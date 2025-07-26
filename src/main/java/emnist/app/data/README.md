
# Dataset Information

The `EMNIST` dataset is a set of images of handwritten digits. The dataset is split into a training set and a test set.

| Dataset Name    | Images        |
| --------------- | ------------- |
| train           | 60000         |
| test            | 10000         |

---

### Fields

- `image`: The image of the handwritten digit. The data type of this field is `image`, values are base64 encoded byte arrays.
- `label`: The label of the handwritten digit. The data type of this field is `label`, values ranging from '0' to '9'.

### Splits

- `train`: The training set consists of 60000 example images.
- `test`: The test set consists of 10000 example images.

---

### Developer Notes:

<dl>
    <dt></dt>
    <dd>Python script to save the images locally.</dd>
</dl>

```bash
python src\main\java\emnist\app\data\save_images.py

```
