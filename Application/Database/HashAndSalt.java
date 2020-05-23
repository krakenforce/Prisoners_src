package Application.Database;

final class HashAndSalt {
    private final String hash;
    private final String salt;

    public HashAndSalt(String h, String s) {
        this.hash = h;
        this.salt = s;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }
}
