import os, librosa, glob, scipy
import tensorflow as tf
import numpy as np
import soundfile as sf
from tensorflow.keras.optimizers import Adam
from .models.tacotron import post_CBHG
from .models.modules import griffin_lim
from .util.hparams import *


checkpoint_dir = './checkpoint/2'
save_dir = './ouput/tmp'
os.makedirs(save_dir, exist_ok=True)
mel_list = glob.glob(os.path.join(save_dir, '*.npy'))


def test_step(mel):
    mel = np.expand_dims(mel, axis=0)
    pred = model(mel, is_training=False)

    pred = np.squeeze(pred, axis=0)
    pred = np.transpose(pred)

    pred = (np.clip(pred, 0, 1) * max_db) - max_db + ref_db
    pred = np.power(10.0, pred * 0.05)
    wav = griffin_lim(pred ** 1.5)
    wav = scipy.signal.lfilter([1], [1, -preemphasis], wav)
    wav = librosa.effects.trim(wav, frame_length=win_length, hop_length=hop_length)[0]
    wav = wav.astype(np.float32)
    sf.write(os.path.join(save_dir, 'test.wav'), wav, sample_rate)
    print('파일이 저장이 안된다??')
    

model = post_CBHG(K=8, conv_dim=[256, mel_dim])
optimizer = Adam()
step = tf.Variable(0)
checkpoint = tf.train.Checkpoint(optimizer=optimizer, model=model, step=step)
checkpoint.restore(tf.train.latest_checkpoint(checkpoint_dir)).expect_partial()
