package com.example.myapplication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.w3c.dom.Text
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class BudgetActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var database: DatabaseReference
    private lateinit var loader: ProgressDialog
    lateinit var totalText: TextView
    private lateinit var uid: String
    private lateinit var data: Data
    private lateinit var recyclerView: RecyclerView
    private var item: String = ""
    private var key: String = ""
    private var amount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)
        totalText = findViewById<TextView>(R.id.totalText)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = true
        recyclerView = findViewById<RecyclerView>(R.id.recylerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("Data").child(firebaseAuth.currentUser?.email.toString().replace(".", ""))
        loader = ProgressDialog(this)

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var total: Int = 0
                for (snap: DataSnapshot in snapshot.children){
                    val data = snap.getValue(Data::class.java)
                    total += data?.getAmount()!!
                    totalText.setText("Total budget: $" + total)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val button = findViewById<FloatingActionButton>(R.id.fab)
        button.setOnClickListener {
            addItem();
        }

    }

    private fun addItem() {
        val alert = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val myView = inflater.inflate(R.layout.input, null)
        alert.setView(myView)

        val dialog = alert.create()
        dialog.setCancelable(false)

        val spinner = myView.findViewById<Spinner>(R.id.spinner)
        val amount = myView.findViewById<EditText>(R.id.amount)
        val cancel = myView.findViewById<Button>(R.id.cancel)
        val save = myView.findViewById<Button>(R.id.save)

        save.setOnClickListener {
            val budgetAmount = amount.text.toString()
            val budgetItem = spinner.selectedItem.toString()
            if(TextUtils.isEmpty(budgetAmount)){
                amount.setError("Amount is required")
            }
            if(budgetItem.equals("Select an Item")){
                Toast.makeText(this, "Select a valid item", Toast.LENGTH_SHORT).show()
            }
            else{
                loader.setMessage("adding a budget item")
                loader.setCanceledOnTouchOutside(false)
                loader.show()

                val cal = Calendar.getInstance()
                val date = cal.get(Calendar.DATE)
                val month = cal.get(Calendar.MONTH) + 1
                val data = Data(budgetItem, date.toString(), firebaseAuth.currentUser?.email.toString().replace(".", ""),Integer.parseInt(budgetAmount), month)
                if (uid.isNotEmpty()) {
                    database.child(budgetItem).setValue(data).addOnCompleteListener { task ->
                        run {
                            if (task.isSuccessful) {
                                Toast.makeText(this, "added Successfully", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(this, "added Failed", Toast.LENGTH_SHORT).show()
                            }
                            loader.dismiss()
                        }
                    }
                }

            }
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun onStart() {
        super.onStart()

        val option = FirebaseRecyclerOptions.Builder<Data>().setQuery(database, Data::class.java).build()
        val adapter = object :FirebaseRecyclerAdapter<Data, MyViewHolder>(option){
            @NonNull
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.data, parent, false)
                return MyViewHolder(view)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Data) {
                holder.setAmountName("Amount: $" + model.getAmount().toString())
                holder.setDate("Date: " + model.getDate())
                holder.setMonth("Month: " + model.getMonth().toString())
                holder.setItemName("Item: " + model.getItem())
                holder.myView.setOnClickListener {
                    key = getRef(position).key!!
                    item = model.getItem()
                    amount = model.getAmount()
                    update()
                }
            }

        }
        recyclerView.adapter = adapter
        adapter.startListening()

    }
    private fun update(){
        val alert = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val myView = inflater.inflate(R.layout.edit, null)
        alert.setView(myView)

        val dialog = alert.create()
        val delete = myView.findViewById<Button>(R.id.delete)
        val save = myView.findViewById<Button>(R.id.save)
        val itemName = myView.findViewById<TextView>(R.id.itemName)
        val amount1 = myView.findViewById<EditText>(R.id.amount)

        itemName.setText(item)
        amount1.setText(amount.toString())
        amount1.setSelection(amount.toString().length)

        save.setOnClickListener {
            val cal = Calendar.getInstance()
            val date = cal.get(Calendar.DATE)
            val month = cal.get(Calendar.MONTH) + 1
            amount = Integer.parseInt(amount1.text.toString())
            val data = Data(item, date.toString(), key, amount, month)
            if (uid.isNotEmpty()) {
                database.child(key).setValue(data).addOnCompleteListener { task ->
                    run {
                        if (task.isSuccessful) {
                            Toast.makeText(this, "edited Successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "edited Failed", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                dialog.dismiss()
            }

        }
        delete.setOnClickListener {
            database.child(key).removeValue().addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful) {
                        Toast.makeText(this, "delete Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "delete Failed", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            dialog.dismiss()

        }
        dialog.show()
    }
    class MyViewHolder: RecyclerView.ViewHolder {
        var myView: View

        constructor (@NonNull itemView: View) : super(itemView) {
            myView = itemView
        }
        fun setItemName(itemName: String){
            val item = myView.findViewById<TextView>(R.id.item)
            item.setText(itemName)
        }
        fun setAmountName(itemAmount: String){
            val item = myView.findViewById<TextView>(R.id.amount)
            item.setText(itemAmount)
        }
        fun setDate(date: String){
            val item = myView.findViewById<TextView>(R.id.date)
            item.setText(date)
        }
        fun setMonth(month: String){
            val item = myView.findViewById<TextView>(R.id.month)
            item.setText(month)
        }
    }

}