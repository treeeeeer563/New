const APK2_URL = "https://github.com/ivan822828/urtierqjw2391/releases/download/v1.0/app-debug.apk";

document.getElementById('downloadBtn').addEventListener('click', function() {
    document.getElementById('updateInfo').style.display = 'none';
    document.getElementById('progressContainer').style.display = 'block';
    if (window.Android) {
        window.Android.startDownload(APK2_URL);
    }
});

function updateProgress(percent) {
    document.getElementById('progressFill').style.width = percent + '%';
    document.getElementById('progressText').textContent = percent + '%';
}

function downloadComplete() {
    document.getElementById('progressContainer').style.display = 'none';
    document.getElementById('completeContainer').style.display = 'block';
    if (window.Android) {
        window.Android.installApk();
    }
}

function downloadError(message) {
    alert('Download error: ' + message);
}