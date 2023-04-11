import React from "react";
import FileManager from "./FileManager";

type PopupProps = {
  isOpen: boolean;
  onClose: () => void;
};

const Popup: React.FC<PopupProps> = ({ isOpen, onClose }) => {
  return (
    <>
      {isOpen && (
        <>
          <div className="popup-overlay" />
          <div className="popupUpload">
          <div className="popup-content">
            <h2>Popup Title</h2>
            <FileManager/>
            <button onClick={onClose}>Close</button>
          </div>
        </div>
        </>
      )}
    </>
  );
};

export default Popup;
