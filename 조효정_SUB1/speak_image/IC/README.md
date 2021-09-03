# SUB1_IC

### Error

>  PY3 => PY37로 변경

```
$ python image_captioning.py 
Traceback (most recent call last):
  File "image_captioning.py", line 26, in <module>
    from maskrcnn_benchmark.utils.model_serialization import load_state_dict
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\utils\model_serialization.py", line 7, in <module>
    from maskrcnn_benchmark.utils.imports import import_file
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\utils\imports.py", line 4, in <module>
    if torch._six.PY3:
AttributeError: module 'torch._six' has no attribute 'PY3'
(conda) 
```



> detectron_model.yaml 경로를 './model_data/detectron_model.yaml'로 변경

```
$ python image_captioning.py 
Hugginface transformers not installed; please visit https://github.com/huggingface/transformers
meshed-memory-transformer not installed; please run `pip install git+https://github.com/ruotianluo/meshed-memory-transformer.git`
Traceback (most recent call last):
  File "image_captioning.py", line 170, in <module>
    caption_model = Caption_Model()
  File "image_captioning.py", line 147, in __init__
    self.feature_extractor = FeatureExtractor()
  File "image_captioning.py", line 43, in __init__
    self.detection_model = self._build_detection_model()
  File "image_captioning.py", line 54, in _build_detection_model
    cfg.merge_from_file('/home/multicam/multicam_project/speak_image/IC/model_data/detectron_model.yaml')
  File "C:\Users\multicampus\anaconda3\envs\conda\lib\site-packages\yacs\config.py", line 211, in merge_from_file
    with open(cfg_filename, "r") as f:
FileNotFoundError: [Errno 2] No such file or directory: '/home/multicam/multicam_project/speak_image/IC/model_data/detectron_model.yaml'
(conda) 
```



>  README에서 checkpoints 파일 설치

```
infos_trans12-best.pkl
model-best.pth
detectron_model.pth
```



> 해결 중..

```
$ python image_captioning.py 
Hugginface transformers not installed; please visit https://github.com/huggingface/transformers
meshed-memory-transformer not installed; please run `pip install git+https://github.com/ruotianluo/meshed-memory-transformer.git`
Traceback (most recent call last):
  File "image_captioning.py", line 173, in <module>
    print(caption_model.inference(input))
  File "image_captioning.py", line 160, in inference
    img_feature = self.feature_extractor(img_feature)
  File "image_captioning.py", line 47, in __call__
    detectron_features = self.get_detectron_features(url)
  File "image_captioning.py", line 138, in get_detectron_features
    output = self.detection_model(current_img_list)
  File "C:\Users\multicampus\anaconda3\envs\conda\lib\site-packages\torch\nn\modules\module.py", line 889, in _call_impl
    result = self.forward(*input, **kwargs)
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\detector\generalized_rcnn.py", line 52, in forward
    proposals, proposal_losses = self.rpn(images, features, targets)
  File "C:\Users\multicampus\anaconda3\envs\conda\lib\site-packages\torch\nn\modules\module.py", line 889, in _call_impl
    result = self.forward(*input, **kwargs)
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\rpn.py", line 96, in forward
    return self._forward_test(anchors, objectness, rpn_box_regression)
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\rpn.py", line 117, in _forward_test
    boxes = self.box_selector_test(anchors, objectness, rpn_box_regression)
  File "C:\Users\multicampus\anaconda3\envs\conda\lib\site-packages\torch\nn\modules\module.py", line 889, in _call_impl
    result = self.forward(*input, **kwargs)
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\inference.py", line 135, in forward
    sampled_boxes.append(self.forward_for_single_feature_map(a, o, b))
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\inference.py", line 115, in forward_for_single_feature_map
    score_field="objectness",
  File "c:\users\multicampus\desktop\sub1\s05p21b105\조효정_sub1\speak_image\ic\vqa_origin\maskrcnn_benchmark\structures\boxlist_ops.py", line 27, in boxlist_nms
    keep = _box_nms(boxes, score, nms_thresh)
RuntimeError: Not compiled with GPU support
(conda) 
```

