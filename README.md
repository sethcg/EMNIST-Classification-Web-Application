# EMNIST Digit Prediction Web Application

## Description:

A web application that takes a user drawn image and uses a convolutional neural network to predict the image's classification as a digit.

### Convolutional Neural Network
<dl>
    <dd>The bulk of this project is the Java Springboot backend, which is a hand-rolled convolutional neural network for EMNIST digit classification. This neural network can train, test, and predict using provided images.</dd>
</dl>

### EMNIST Data
<dl>
    <dd>The <a href="https://www.nist.gov/itl/products-and-services/emnist-dataset">EMNIST dataset</a> is a set of handwritten digits split into 60,000 training and 10,000 testing images.</dd>
</dl>
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

---

## Application Demo:
<details open>
  <summary>Show</summary>
  <img src="https://github.com/user-attachments/assets/00cc0377-1a73-4158-b9af-e6f2b7a62b27" width="800" height="532">
  <br>
</details>

### Screenshots:
<details close>
  <summary>Show</summary>
  <img src="https://github.com/user-attachments/assets/7b99ea1a-4438-4eae-b4ed-f2d2fd02c84b" width="450" height="348">
  <img src="https://github.com/user-attachments/assets/59a096f5-9301-4001-ad3c-9d75a8907a32" width="450" height="348">
  <img src="https://github.com/user-attachments/assets/a29e7ec1-e69e-4a7f-b857-42576c47b9bd" width="450" height="348">
</details>

---

## Features:

#### Frontend:
- [x] Vue
- [x] Vite
- [x] Tailwind
- [x] Formatting (Prettier)

#### Backend:
- [x] Java
- [x] Maven
- [x] SpringBoot
- [x] Parquet (EMNIST Data) 

---

## Developer Notes:

```bash

# PACKAGE THE APPLICATION
mvn clean package

# PACKAGE THE APPLICATION (SKIP TESTS)
mvn clean package -DskipTests

# RUN APPLICATION ON (http://localhost:8080)
mvn spring-boot:run &

```

---

### Resources Used:

| Source                                                                                                                          | Description                                                |
| :------------------------------------------------------------------------------------------------------------------------------ | :--------------------------------------------------------- |
| [Part One](https://victorzhou.com/blog/intro-to-cnns-part-1/) and [Part Two](https://victorzhou.com/blog/intro-to-cnns-part-2/) | Tutorial by Victor Zhou on convolutional neural networks   |
| [Blog](https://medium.com/thedeephub/convolutional-neural-networks-a-comprehensive-guide-5cc0b5eae175)                          | Explanation of convolutional neural networks               |
| [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin)                                                      | Helps integrate the frontend and backend                   |


<br />

> The beginning of wisdom is: Acquire wisdom; And with all your acquiring, get understanding.
