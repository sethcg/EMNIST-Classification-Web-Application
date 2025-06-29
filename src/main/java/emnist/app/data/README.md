---
dataset_info:
  config_name: emnist
  features:
  - name: image
    dtype: image
  - name: label
    dtype:
      class_label:
        names:
          '0': '0'
          '1': '1'
          '2': '2'
          '3': '3'
          '4': '4'
          '5': '5'
          '6': '6'
          '7': '7'
          '8': '8'
          '9': '9'
  splits:
  - name: train
    num_bytes: 25332252.0
    num_examples: 60000
  - name: test
    num_bytes: 4230499.0
    num_examples: 10000
  download_size: 28641083
  dataset_size: 29562751.0
configs:
- config_name: emnist
  data_files:
  - split: train
    path: data/train-*
  - split: test
    path: data/test-*
  default: true
---

## Dataset Information

The `emnist` dataset is a set of images of handwritten digits. The dataset is split into a training set and a test set.

## Data Fields

- `image`: The image of the handwritten digit. The data type of this field is `image`.
- `label`: The label of the handwritten digit. The data type of this field is `class_label`, and it can take on the values '0' to '9'.

## Data Splits

- `train`: The training set consists of 60000 examples, with a total size of 25579752 bytes.
- `test`: The test set consists of 10000 examples, with a total size of 4271749 bytes.
