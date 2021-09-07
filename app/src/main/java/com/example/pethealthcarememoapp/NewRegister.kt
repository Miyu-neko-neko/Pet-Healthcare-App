package com.example.pethealthcarememoapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.pethealthcarememoapp.databinding.NewRegisterBinding
import io.realm.Realm
import java.util.*

class NewRegister : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: NewRegisterBinding

    lateinit var realm: Realm
    var dateDayItem: String = ""
    var yearItem: String = ""
    var monthItem: String = ""
    var strPetName: String = ""
    var Date_str: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.new_register)
        binding = NewRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerYear: Spinner = binding.spinnerYear
        val spinnerMonth: Spinner = binding.spinnerMonth
        val spinnerDate: Spinner = binding.spinnerDate

        //xmlファイルからアイテムの配列を取得
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

        spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //parentのspinnerを指定
                //選択されたitemを取得
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
                id: Long
            ) {
                //parentのspinnerを指定
                //選択されたitemを取得
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
                id: Long
            ) {
                //parentのspinnerを指定
                //選択されたitemを取得
                yearItem = parent.selectedItem as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btRegister.setOnClickListener(this)

        binding.btGoBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {

        var realm = Realm.getDefaultInstance()

// 初期化
        var Id = 1

// userIdの最大値を取得
        val maxUserId: Number? = realm.where(TaskDB::class.java).max("id")

// 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if (maxUserId != null) {  //PrimaryKeyをインクリメントする
            Id = maxUserId.toInt() + 1
        }
        val etPersonName = binding.etPersonName
        strPetName = etPersonName.text.toString()
        etPersonName.setText("")

        val strYear = yearItem
        val strMonth = monthItem
        val strDate = dateDayItem

        Date_str = strYear + strMonth + strDate

        val rbBoy = binding.radioButtonBoy
        val rbGirl = binding.radioButtonGirl

        realm.beginTransaction()//データベースの使用開始
        val taskDB = realm.createObject(TaskDB::class.java, Id)
        Log.v("NewRegister", "Id= " + Id + "")
        taskDB.realm_strPetName = strPetName
        Log.v("NewRegister", "strPetName= " + strPetName + "")
        taskDB.realm_Date_str = Date_str
        Log.v("NewRegister", "Date_str= " + Date_str + "")

        if (rbBoy.isChecked){
            taskDB.realm_strSex = getString(R.string.boy)
        }

        if (rbGirl.isChecked){
            taskDB.realm_strSex = getString(R.string.girl)
        }

        realm.commitTransaction()//データベースの使用を終了する

        Toast.makeText(this@NewRegister, "登録が完了しました", Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()
        realm = Realm.getDefaultInstance()

    }

    override fun onPause() {
        super.onPause()
        realm.close()
    }

}