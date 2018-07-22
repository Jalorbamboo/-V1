package cn.fsyz.shishuo.Bean;

import cn.bmob.v3.BmobRole;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Jalor on 2018/4/14.
 */

public class Role extends BmobRole {
    public Role(String name) {
        super(name);
    }

    private BmobRelation users;

    @Override
    public BmobRelation getUsers() {
        return users;
    }

    public void setUsers(BmobRelation users) {
        this.users = users;
    }
}
