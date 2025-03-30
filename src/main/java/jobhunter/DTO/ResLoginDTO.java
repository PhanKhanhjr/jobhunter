package jobhunter.DTO;

public class ResLoginDTO {
    private String access_token;
    private UserLogin userlogin;

    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UserLogin getUserlogin() {
        return userlogin;
    }

    public void setUserlogin(UserLogin userlogin) {
        this.userlogin = userlogin;
    }

    //inner class
    public static class UserLogin{
        private long id;
        private String email;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public UserLogin( long id, String email,String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }
        public UserLogin() {

        }
    }
}
