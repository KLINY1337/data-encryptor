import React, { useState, useEffect } from 'react';

interface ResponseData {
  isVaultCreated: boolean;
}

function DisplayResponse() {
  const [responseData, setResponseData] = useState<ResponseData>();

  useEffect(() => {
    fetch('http://localhost:8080/isKeyStoreExist', { mode: 'cors' })
      .then((response) => response.json())
      // eslint-disable-next-line promise/always-return
      .then((data) => {
        setResponseData(data);
      })
      .catch((error) => console.error(error));
  }, []);

  return (
    <div>
      {responseData ? (
        // Display the response data if it exists
        <p>{JSON.stringify(responseData)}</p>
      ) : (
        // Display a loading message if the response data doesn't exist yet
        <p>Loading...</p>
      )}
    </div>
  );
}

export default DisplayResponse;
