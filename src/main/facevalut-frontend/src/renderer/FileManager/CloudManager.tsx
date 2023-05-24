import React, { useEffect, useState } from 'react';
import EncodedFileList from 'renderer/EncodedFileList';
import './ManagerStyle.css';
import Popup from './UploadPopup';
import logowhite from '/images/facevaultwhite.png';
import logoblack from '/images/facevaultblack.png';

function CloudManager() {
  const [popupOpen, setPopupOpen] = useState(false);

  const [numUploadedFiles, setNumUploadedFiles] = useState(0);

  const [manualSect, setManualSect] = useState(false);

  const handleButtonClick = () => {
    setPopupOpen(true);
  };

  const handleClosePopup = () => {
    setPopupOpen(false);
  };

  const handleManual = () => {
    setManualSect(!manualSect);
  }

  // @ts-ignore
  const handleDownload = async (event) => {
    event.preventDefault();
    console.log(event.target.text.value);
    try {
      const msg = `name=${encodeURIComponent(event.target.text.value)}`;
      const response = await fetch(
        `http://localhost:8080/downloadFile?${msg}`,
        {
          mode: 'cors',
          method: 'POST',
          // body: JSON.stringify({"name":event.target.text.value}),
        }
      );
      const data = await response.json();
      console.log(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchNumFiles = async () => {
      try {
        const response = await fetch(
          'http://localhost:8080/getLinkListLength',
          {
            mode: 'cors',
          }
        );
        if (!response.ok) {
          throw new Error('Failed to fetch data');
        }
        const data = await response.json();
        setNumUploadedFiles(data);
      } catch (error) {
        console.error(error);
      }
    };

    fetchNumFiles();
  }, []);
  return (
    <div className="MainApp">
      {/*<div className="leftsection">*/}
      {/*  <div className="leftsection__header">*/}
      {/*    <div className="iconcircle" />*/}
      {/*    <h1>User</h1>*/}
      {/*  </div>*/}

      {/*  <div className="leftsection__body">*/}
      {/*    <p>Your total uploaded files: {numUploadedFiles}</p>*/}
      {/*  </div>*/}
      {/*</div>*/}

      <div className="rightsection">
        <div className="rightsection__header">
          <div className="logo">
            <img src={logoblack} alt="logo" height="70px"/>
            <h1 className="filesHeader">Your FaceVault</h1>

          </div>

          {!popupOpen && (
            <button onClick={handleButtonClick}>
              Encrypt
            </button>
          )}

          {popupOpen && (
            <div className="popupsect">
              <Popup  isOpen={popupOpen} onClose={handleClosePopup} />
            </div>
          )}

        </div>

        <div className="rightsection__body">
          <EncodedFileList />

          <div className="footer">
            <h2>Amount of encrypted files: {numUploadedFiles}</h2>
            <h2 className="manualDownloadHeader" onClick={handleManual}>Download manually</h2>
            {manualSect && (
              <div>
                <p>Enter filename from section above and submit</p>
                <form onSubmit={handleDownload} className="downloadForm">
                  <input type="text" name="text" />
                  <input type="submit" value="Submit" />
                </form>
              </div>
            )}
          </div>

        </div>
      </div>
    </div>
  );
}

export default CloudManager;
