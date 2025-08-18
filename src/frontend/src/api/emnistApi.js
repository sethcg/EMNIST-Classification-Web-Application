/**
 * Call the EMNIST prediction API to ensure a connection has been made.
 * @param  {Function} callback - The function that handles the returned message.
 */
export let ping = async (callback) => {
  await fetch('/api/ping', { method: 'POST' })
    .then((response) => response.text())
    .then((data) => callback(data))
    .catch((error) => console.error(error));
};

/**
 * Call the EMNIST prediction API to classify the drawn image into a number between 0-9.
 * @param  {number[][]} image - The image data being classified.
 * @param  {Function} callback - The function that handles the returned message.
 */
export let predict = async (image, callback) => {
  await fetch('/api/predict', {
    method: 'POST',
    body: JSON.stringify(image),
    headers: { 'Content-Type': 'application/json; charset=utf-8' },
  })
    .then((response) => response.text())
    .then((data) => callback(data))
    .catch((error) => console.error(error));
};

/**
 * Call the EMNIST prediction API to train the convolutional neural network.
 * @param  {Function} callback - The function that handles post training processes.
 */
export let train = async (callback) => {
  await fetch('/api/train', { method: 'POST' })
    .then((response) => response.text())
    .then(() => callback())
    .catch((error) => console.error(error));
};

/**
 * Call the EMNIST prediction API to test the convolutional neural network.
 * @param  {Function} callback - The function that handles post testing processes.
 */
export let test = async (callback) => {
  await fetch('/api/test', { method: 'POST' })
    .then((response) => response.text())
    .then(() => callback())
    .catch((error) => console.error(error));
};

/**
 * Call the EMNIST prediction API to get the current convolutional neural network saved training results.
 * @param  {Function} callback - The function that handles the returned training results.
 * @param  {Function} handleError - The function that handles errors.
 */
export let trainingResults = async (callback, handleError) => {
  await fetch('/api/trainingStats', { method: 'POST' })
    .then((response) => response.text())
    .then((json) => {
      const data = JSON.parse(json);
      callback(data);
    })
    .catch((error) => handleError(error));
};

/**
 * Call the EMNIST prediction API to get the current convolutional neural network saved testing results.
 * @param  {Function} callback - The function that handles the returned testing results.
 * @param  {Function} handleError - The function that handles errors.
 */
export let testingResults = async (callback, handleError) => {
  await fetch('/api/testingStats', { method: 'POST' })
    .then((response) => response.text())
    .then((json) => {
      const data = JSON.parse(json);
      callback(data);
    })
    .catch((error) => handleError(error));
};
