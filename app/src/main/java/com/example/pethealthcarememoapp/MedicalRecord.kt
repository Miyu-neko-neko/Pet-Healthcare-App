package com.example.pethealthcarememoapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.pethealthcarememoapp.databinding.MedicalRecordBinding
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class MedicalRecord : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemLongClickListener {

    private lateinit var binding: MedicalRecordBinding

    lateinit var medical_realm: Realm
    lateinit var medical_result: RealmResults<TaskDB2>  //データの塊(コレクション)
    lateinit var medical_list: ArrayList<String>
    lateinit var  medical_adapter: ArrayAdapter<String>
    var dateDayItem: String = ""
    var yearItem: String = ""
    var monthItem: String = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.medical_record)
        binding = MedicalRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val strName = bundle?.getString(getString(R.string.intent_key_name))
        val selectedId = bundle?.getString(getString(R.string.IntentSelectedId))
        binding.tvSelectedName.text = strName + "  のカルテ" + "    " + "No. " +  selectedId


        val spinnerYear: Spinner = binding.spinnerYear2
        val spinnerMonth: Spinner = binding.spinnerMonth2
        val spinnerDate: Spinner = binding.spinnerDate2

        val items = resources.getStringArray(R.array.month_array)
        val items2 = resources.getStringArray(R.array.year_array)
        val items3 = resources.getStringArray(R.array.date_array)

        //アダプターにアイテム配列を設定
        val Adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        val Adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items2)
        val Adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items3)

        //スピナーにアダプターを設定
        spinnerMonth.adapter = Adapter
        spinnerYear.adapter = Adapter2
        spinnerDate.adapter = Adapter3

        val today = Calendar.getInstance()
        var yearPosition: Int = 0

        spinnerYear.setSelection(today.get(Calendar.YEAR))
        if (today.get(Calendar.YEAR) == 2021) {
            yearPosition = 20
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2022) {
            yearPosition = 21
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2023) {
            yearPosition = 22
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2024) {
            yearPosition = 23
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2025) {
            yearPosition = 24
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2026) {
            yearPosition = 25
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2027) {
            yearPosition = 26
            spinnerYear.setSelection(yearPosition)
        }

        if (today.get(Calendar.YEAR) == 2028) {
            yearPosition = 27
            spinnerYear.setSelection(yearPosition)
        }

        spinnerMonth.setSelection(today.get(Calendar.MONTH))
        //Log.v("MedicalRecord", "today.get(Calendar.MONTH= " + today.get(Calendar.MONTH)+ "")
        spinnerDate.setSelection(today.get(Calendar.DATE))

        spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                dateDayItem = parent.selectedItem as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                monthItem = parent.selectedItem as String

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                yearItem = parent.selectedItem as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btRegister2.setOnClickListener(this)
        binding.medicalListView.setOnItemLongClickListener(this)

        binding.btGoBack2.setOnClickListener {
            finish()
        }

    }

    override fun onClick(v: View?) {

        val tvSelectedName = binding.tvSelectedName
        val strNameId = tvSelectedName.text.toString()

        val strYear = yearItem
        val strMonth = monthItem
        val strDate = dateDayItem
        val Date_str = strYear + strMonth + strDate

        val etWeight = binding.etWeight
        val str_etWeight = etWeight.text.toString()
        etWeight.setText("")

        val editText = binding.editText
        val str_editText = editText.text.toString()
        editText.setText("")

        medical_realm.beginTransaction()//データベースの使用開始
        val taskDB = medical_realm.createObject(TaskDB2::class.java)
        taskDB.realm_strName = strNameId
        Log.v("MedicalRecord", "strName= " + strNameId + "")
        taskDB.realm_dateOfMedicalçare = Date_str
        Log.v("MedicalRecord", "Date_str= " + Date_str + "")
        taskDB.realm_weight = str_etWeight
        Log.v("MedicalRecord", "str_etWeight= " + str_etWeight + "")
        taskDB.realm_medicalDescri = str_editText
        Log.v("MedicalRecord", "str_editText= " + str_editText + "")

        medical_list.add(0, Date_str + "    " + str_etWeight + "    " + "\n" + str_editText)
        binding.medicalListView.adapter = medical_adapter

        medical_realm.commitTransaction()//データベースの使用を終了する

    }


    override fun onResume() {
        super.onResume()
        //インスタンスを取得してRealmの使用を可能にする
        medical_realm = Realm.getDefaultInstance()

        val tvSelectedName = binding.tvSelectedName
        val strName = tvSelectedName.text.toString()

        medical_result = medical_realm.where(TaskDB2::class.java)
            .contains("realm_strName", strName)
            .findAll()
            .sort("realm_dateOfMedicalçare", Sort.DESCENDING)
        //Log.v("MedicalRecord", "medical_result= " + medical_result + "")
        //medical_result = medical_realm.where(TaskDB2::class.java).findAll().sort("realm_dateOfMedicalçare", Sort.DESCENDING)

        //データの塊(コレクション)の種類
        medical_list = ArrayList()

        //取得したデータの行数
        val length = medical_result.size

        for (i in 0..length - 1) {
            medical_list.add(medical_result[i]!!.realm_dateOfMedicalçare + "    " + medical_result[i]!!.realm_weight + "    " + "\n" + medical_result[i]!!.realm_medicalDescri)
        }

        medical_adapter = ArrayAdapter(this@MedicalRecord,android.R.layout.simple_list_item_1,medical_list)
        binding.medicalListView.adapter = medical_adapter
    }

    override fun onPause() {
        super.onPause()
        //Realmの使用をやめる
        medical_realm.close()
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long,
    ): Boolean {
        val selectDB = medical_result[position]!!
        //AlertDialog.Builderのインスタンス化
        AlertDialog.Builder(this@MedicalRecord).apply {
            setTitle(selectDB.realm_dateOfMedicalçare + "の削除")
            setMessage("削除しても良いですか？")
            setPositiveButton("yes") {
                //Yesボタン押した時の処理
                    dialog, which ->

                //長押しした行のデータの削除
                medical_realm.beginTransaction()
                selectDB.deleteFromRealm()
                medical_realm.commitTransaction()

                //長押しした行のリストの削除
                medical_list.removeAt(position)
                //画面更新、アダプターの再接続
                binding.medicalListView.adapter = medical_adapter
            }
            setNegativeButton("no") {
                    dialog, which ->
            }
            show()
        }
        return true
    }
}