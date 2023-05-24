import React from "react";
import FileManager from "./FileManager";

type PopupProps = {
  isOpen: boolean;
  onClose: () => void;
};

const Popup: React.FC<PopupProps> = ({ isOpen, onClose }) => {
  return (
    <div className={"popup"}>
      {isOpen && (
        <>
          <div className="popup-overlay" />
          <div className="popupUpload">
          <div className="popup-content">
            <FileManager/>
            <button onClick={onClose}>Close</button>
          </div>
        </div>
        </>
      )}
    </div>
  );
};

export default Popup;
