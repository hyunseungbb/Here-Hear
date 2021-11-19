import tensorflow as tf
import numpy as np
from jamo import hangul_to_jamo
from .models.tacotron import Tacotron
from .util.hparams import *
from .util.text import sequence_to_text, text_to_sequence
from . import test22
# sentences = '정말로 사랑한담 기다려주세요'


checkpoint_dir = './checkpoint/1'
save_dir = './output'


def test_step(text):
    seq = text_to_sequence(text)
    enc_input = np.asarray([seq], dtype=np.int32)
    sequence_length = np.asarray([len(seq)], dtype=np.int32)
    dec_input = np.zeros((1, max_iter, mel_dim), dtype=np.float32)

    pred = []
    for i in range(1, max_iter+1):
        mel_out, alignment = model(enc_input, sequence_length, dec_input, is_training=False)
        if i < max_iter:
            dec_input[:, i, :] = mel_out[:, reduction * i - 1, :]
        pred.extend(mel_out[:, reduction * (i-1) : reduction * i, :])

    pred = np.reshape(np.asarray(pred), [-1, mel_dim])
    test22.test_step(pred)

def naive_tts_run(sentences):
    checkpoint = tf.train.Checkpoint(model=model)
    checkpoint.restore(tf.train.latest_checkpoint(checkpoint_dir)).expect_partial()


    jamo = ''.join(list(hangul_to_jamo(sentences)))
    test_step(jamo)

model = Tacotron(K=16, conv_dim=[128, 128])
# checkpoint = tf.train.Checkpoint(model=model)
# checkpoint.restore(tf.train.latest_checkpoint(checkpoint_dir)).expect_partial()


# jamo = ''.join(list(hangul_to_jamo(sentences)))
# test_step(jamo)