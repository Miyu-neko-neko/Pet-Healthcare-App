package com.example.pethealthcaresample

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

//Realmの型を決める
@Suppress("NonAsciiCharacters")
open class TaskDB : RealmObject() {
//登録情報
@PrimaryKey
    open var id: Int? = null
    open var realm_strPetName: String = ""
    open var realm_Date_str: String = ""
    open var realm_strSex: String = ""

}