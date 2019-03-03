package google.zxing.integration.android;
public final class IntentResult {
    private final String contents;
    private final String formatName;
    private final byte[] rawBytes;
    private final Integer orientation;
    private final String errorCorrectionLevel;
    IntentResult() {
        this(null, null, null, null, null);
    }
    IntentResult(String contents,
                 String formatName,
                 byte[] rawBytes,
                 Integer orientation,
                 String errorCorrectionLevel) {
        this.contents = contents;
        this.formatName = formatName;
        this.rawBytes = rawBytes;
        this.orientation = orientation;
        this.errorCorrectionLevel = errorCorrectionLevel;
    }
    @SuppressWarnings("unused")
    public String getContents() {
        return contents;
    }
    @SuppressWarnings("unused")
    public String getFormatName() {
        return formatName;
    }
    @SuppressWarnings("unused")
    public Integer getOrientation() {
        return orientation;
    }
    @Override
    public String toString() {
        int rawBytesLength = rawBytes == null ? 0 : rawBytes.length;
        return "Format: " + formatName + '\n' +
                "Contents: " + contents + '\n' +
                "Raw bytes: (" + rawBytesLength + " bytes)\n" +
                "Orientation: " + orientation + '\n' +
                "EC level: " + errorCorrectionLevel + '\n';
    }
}