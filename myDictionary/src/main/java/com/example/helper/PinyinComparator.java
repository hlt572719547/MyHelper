package com.example.helper;

import java.util.Comparator;

import com.example.entity.UserInfo;

public class PinyinComparator implements Comparator<UserInfo> {

    public PinyinComparator() {
        super();
    }

    @Override
    public int compare(UserInfo lhs, UserInfo rhs) {
        // TODO Auto-generated method stub
        String str1 = PinyinUtils.getPingYin(lhs.getSortFirstLetter());
        String str2 = PinyinUtils.getPingYin(rhs.getSortFirstLetter());
        return str1.compareTo(str2);
    }

}
