---

  data:
    - name: train
      images: 60000
    - name: test
      images: 10000
      
---

```bash

# SAVE IMAGES LOCALLY
python src\main\java\emnist\app\data\save_images.py

```

## Dataset Information

The `EMNIST` dataset is a set of images of handwritten digits. The dataset is split into a training set and a test set.

## Data Fields

- `image`: The image of the handwritten digit. The data type of this field is `image`.
- `label`: The label of the handwritten digit. The data type of this field is `label`, and it can take on the values '0' to '9'.

## Data Splits

- `train`: The training set consists of 60000 example images.
- `test`: The test set consists of 10000 example images.
