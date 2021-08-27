package com.example.pethealthcaresample

import io.realm.RealmObject

@Suppress("NonAsciiCharacters")
open class TaskDB2 : RealmObject() {
//診察内容入力

    open var realm_strName: String = ""
    open var realm_dateOfMedicalçare: String = ""
    open var realm_weight: String = ""
    open var realm_medicalDescri: String = ""
}

