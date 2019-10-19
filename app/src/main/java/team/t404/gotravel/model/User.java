/**
 *
 */
package team.t404.gotravel.model;

/**
 * @Description: 用户信息表(user)
 * @date 2019年8月8日 下午11:52:50
 */
public class User {
    private int userid;
    private String phone;
    private String password;
    private String name;
    private String gender;
    private int age;
    private String image;
    private int logins;

    public User(String phone, String name, String gender, int age, String image) {
        super();
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.image = image;
    }

    public int getUserid() {
        return userid;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }

    public int getLogins() {
        return logins;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLogins(int logins) {
        this.logins = logins;
    }

    public User(int userid, String phone, String password, String name, String gender, int age, String image, int logins) {
        super();
        this.userid = userid;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.image = image;
        this.logins = logins;
    }
}
