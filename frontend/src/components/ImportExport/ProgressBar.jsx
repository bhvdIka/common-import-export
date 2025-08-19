import React from 'react';

const ProgressBar = ({ progress, status, showPercentage = true }) => {
  const progressValue = Math.max(0, Math.min(100, progress || 0));

  return (
    <div className="progress-bar-container">
      {status && (
        <div className="progress-status">{status}</div>
      )}
      <div className="progress-bar">
        <div 
          className="progress-fill"
          style={{ width: `${progressValue}%` }}
        >
          {showPercentage && progressValue > 20 && (
            <span className="progress-text">{Math.round(progressValue)}%</span>
          )}
        </div>
      </div>
      {showPercentage && progressValue <= 20 && (
        <div className="progress-percentage">{Math.round(progressValue)}%</div>
      )}
    </div>
  );
};

export default ProgressBar;