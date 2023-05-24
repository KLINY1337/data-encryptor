import React, { useState, useEffect } from 'react';

const handleDownload = async (encoded: any) => {
  // event.preventDefault();
  try {
    const msg = `name=${encoded}`;
    const response = await fetch(`http://localhost:8080/downloadFile?${msg}`, {
      mode: 'cors',
      method: 'POST',
      // body: JSON.stringify({"name":event.target.text.value}),
    });
    const data = await response.json();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
};

function EncodedFileList() {
  const [responseData, setResponseData] = useState(null);
  const fetchData = async () => {
    try {
      const response = await fetch('http://localhost:8080/getLinkList', {
        mode: 'cors',
      });
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      const data = await response.json();
      setResponseData(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    let interval = setInterval(() => {
      fetchData();

    },2000)

    return () => clearInterval(interval);

  }, []);

  // @ts-ignore
  return (
    <div>
      {responseData ? (
        <div className="tableDiv">
          <table className={"fileTable"}>
            <thead>
            <tr>
              <th>Name</th>
              <th>EncryptedName</th>
              <th>Vault</th>
            </tr>
            </thead>
            <tbody>
            {responseData.map((item) => (

              <tr>
                <td>{item.decryptedFileName}</td>
                <td>{item.encryptedFileName}</td>
                <td>
                  <button
                    type="button"
                    onClick={() => handleDownload(item.encryptedFileName)}
                  >
                    Decrypt
                  </button>
                </td>
              </tr>
            ))}
            </tbody>
          </table>
        </div>

      ) : (
        <div>Loading...</div>
      )}
    </div>
  );
}

export default EncodedFileList;
