package com.example.pethealthcaresample

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.pethealthcaresample.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmResults

class MainActivity : AppCompatActivity(), AdapterView.OnItemLongClickListener,
    AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    lateinit var realm: Realm
    lateinit var result: RealmResults<TaskDB>  //データの塊(コレクション)
    lateinit var task_list: ArrayList<String>
    lateinit var  adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, NewRegister::class.java)
            startActivity(intent)
        }

        binding.listView.setOnItemClickListener(this)
        binding.listView.setOnItemLongClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        //インスタンスを取得してRealmの使用を可能にする
        realm = Realm.getDefaultInstance()

        result = realm.where(TaskDB::class.java).findAll().sort("realm_Date_str")

        //データの塊(コレクション)の種類
        task_list = ArrayList()

        //取得したデータの行数
        val length = result.size

        for (i in 0..length - 1) {
            task_list.add(result[i]!!.realm_strPetName + "    " + result[i]!!.realm_Date_str + "    " + result[i]!!.realm_strSex)
        }

        adapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_list_item_1, task_list)
        binding.listView.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        //Realmの使用をやめる
        realm.close()
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long,
    ): Boolean {
        //選択した行番号が格納されている
        val selectDB = result[position]!!
        //AlertDialog.Builderのインスタンス化
        AlertDialog.Builder(this@MainActivity).apply {
            setTitle(selectDB.realm_strPetName + "の削除")
            setMessage("削除しても良いですか？")
            setPositiveButton("yes") {
                //Yesボタン押した時の処理
                    dialog, which ->

                //長押しした行のデータの削除
                realm.beginTransaction()
                selectDB.deleteFromRealm()
                realm.commitTransaction()

                //長押しした行のリストの削除
                task_list.removeAt(position)
                //画面更新、アダプターの再接続
                binding.listView.adapter = adapter
            }
            setNegativeButton("no") {
                    dialog, which ->

            }
            show()
        }

        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val strSelectName = result[position]?.realm_strPetName
        val selectedId = result[position]?.id.toString()
        val intent = Intent(this@MainActivity, MedicalRecord::class.java)

        intent.putExtra(getString(R.string.intent_key_name), strSelectName)
        intent.putExtra(getString(R.string.IntentSelectedId), selectedId)


        startActivity(intent)
    }

}