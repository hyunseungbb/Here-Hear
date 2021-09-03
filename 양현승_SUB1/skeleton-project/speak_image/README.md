# Image Captioning

## build

1. anaconda 가상환경에서 의존성 설치
2. requirements.txt  설치

3. `python image_captioning.py` 로 실행

## 오류

1.

```python
# torch._.PY3
torch._.PY37 로 수정
```

2.

```
Traceback (most recent call last):  
  File "image_captioning.py", line 176, in <module>
    print(my_caption_model.inference(input))
  File "image_captioning.py", line 160, in inference
    img_feature = self.feature_extractor(img_feature)
  File "image_captioning.py", line 47, in __call__
    detectron_features = self.get_detectron_features(url)
  File "image_captioning.py", line 138, in get_detectron_features
    output = self.detection_model(current_img_list)
  File "C:\Users\multicampus\anaconda3\envs\venv\lib\site-packages\torch\nn\modules\module.py", line 889, in _call_impl
    result = self.forward(*input, **kwargs)
  File "c:\users\multicampus\skeleton-project\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\detector\generalized_rcnn.py", line 52, in forward
    proposals, proposal_losses = self.rpn(images, features, targets)
  File "C:\Users\multicampus\anaconda3\envs\venv\lib\site-packages\torch\nn\modules\module.py", line 889, in _call_impl
    result = self.forward(*input, **kwargs)
  File "c:\users\multicampus\skeleton-project\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\rpn.py", line 96, in forward
    return self._forward_test(anchors, objectness, rpn_box_regression)
  File "c:\users\multicampus\skeleton-project\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\rpn.py", line 117, in _forward_test
    boxes = self.box_selector_test(anchors, objectness, rpn_box_regression)
  File "C:\Users\multicampus\anaconda3\envs\venv\lib\site-packages\torch\nn\modules\module.py", line 889, in _call_impl
    result = self.forward(*input, **kwargs)
  File "c:\users\multicampus\skeleton-project\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\inference.py", line 135, in forward
    sampled_boxes.append(self.forward_for_single_feature_map(a, o, b))
  File "c:\users\multicampus\skeleton-project\speak_image\ic\vqa_origin\maskrcnn_benchmark\modeling\rpn\inference.py", line 115, in forward_for_single_feature_map
    score_field="objectness",
  File "c:\users\multicampus\skeleton-project\speak_image\ic\vqa_origin\maskrcnn_benchmark\structures\boxlist_ops.py", line 27, in boxlist_nms
    keep = _box_nms(boxes, score, nms_thresh)
RuntimeError: Not compiled with GPU support
```

nvidia에서 cuda 11.4를 다운로드 받아 설치한다.

`python setup.py build develop` 로 다시 build 한 후 `python image_captioning.py`를 하면 이미지캡셔닝이 잘 동작하는 것을 확인할 수 있었다.

 https://developer.nvidia.com/cuda-downloads?target_os=Windows&target_arch=x86_64 



## 실행 결과

![zebra](README.assets\zebra.jpeg)

![image-20210903094842192](README.assets\image-20210903094842192.png)

