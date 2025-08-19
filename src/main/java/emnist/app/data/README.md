
# Dataset Information

The `EMNIST` dataset is a set of images of handwritten digits. The dataset is split into a training set and a test set.

<ul>
    <img width="28" height="28" alt="00008" src="https://github.com/user-attachments/assets/7f2271c2-7c42-4c6d-bc35-7cf8c0541e71" />
    <img width="28" height="28" alt="00003" src="https://github.com/user-attachments/assets/d3aa25aa-72f4-4e04-b227-c4b67233c1d2" />
    <img width="28" height="28" alt="00079" src="https://github.com/user-attachments/assets/0c1308b6-095e-4d9d-bbac-e2af1ba66772" />
    <img width="28" height="28" alt="00024" src="https://github.com/user-attachments/assets/f18b3169-5614-417c-8334-b4692c69a916" />
    <img width="28" height="28" alt="00000" src="https://github.com/user-attachments/assets/9d700dcc-fa72-4cae-8662-d44659f4d9d3" />
    <img width="28" height="28" alt="00023" src="https://github.com/user-attachments/assets/bab5ad19-ca2f-40cb-9c81-0befccd95fbf" />
    <img width="28" height="28" alt="00022" src="https://github.com/user-attachments/assets/6094ca4a-3d5a-4ccf-a579-f51801ba2f50" />
    <img width="28" height="28" alt="00025" src="https://github.com/user-attachments/assets/b60c5d41-1b1c-4ac3-a79a-1b9b772b7181" />
    <img width="28" height="28" alt="00044" src="https://github.com/user-attachments/assets/f57d842d-8919-4513-9d08-27e11c1d418a" />
    <img width="28" height="28" alt="00051" src="https://github.com/user-attachments/assets/23a2ee33-f44a-4bea-b98e-18b13c115f58" />
</ul>

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
