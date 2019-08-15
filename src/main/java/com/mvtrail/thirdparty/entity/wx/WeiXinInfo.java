package com.mvtrail.thirdparty.entity.wx;

/**
 * @author ansen
 * @create time 2017-09-14
 */
public class WeiXinInfo {
    //用户唯一ID
    private String openid;
    //性别obj.getSex() == 1 ? "男" : "女"
    private int sex;
    private String headimgurl;//用户头像URL
    private String nickname = "";
    private int age;


    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "WeiXinInfo{" +
                "openid='" + openid + '\'' +
                ", sex=" + sex +
                ", headimgurl='" + headimgurl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}
