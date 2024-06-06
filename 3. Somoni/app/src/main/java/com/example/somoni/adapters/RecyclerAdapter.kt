package com.example.somoni.adapters

import android.content.Context
import android.os.Build
import android.view.Gravity.END
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.somoni.ConvertFragment
import com.example.somoni.R
import com.example.somoni.databinding.ItemRecyclerFullBinding
import com.example.somoni.setting.ConstVariable
import com.example.somoni.retrofit.ResponseObject
import com.example.somoni.setting.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.example.somoni.ViewToIntentAsBitmap

class RecyclerAdapter(private val number: Int, context: Context, fragmentManager: FragmentManager) :
    ListAdapter<ResponseObject, RecyclerAdapter.AdapterViewHolder>(RecyclerDiffUtil) {

    var clickItem: (ResponseObject) -> (Unit) = {
        val dialog = BottomSheetDialog(context)
        val btnSheetDialog = ItemRecyclerFullBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(btnSheetDialog.root)

        dialog.findViewById<ImageView>(R.id.closeButton)?.setOnClickListener {
            dialog.dismiss()
        }

        btnSheetDialog.convertButton.setOnClickListener { _ ->
            ConvertFragment(it).show(fragmentManager, "TEST_TAG")
        }

        Picasso
            .get()
            .load(it.icon)
            .into(btnSheetDialog.bankIcon)

        btnSheetDialog.bankName.text = it.bankName

        if (it.currency!!.size > 2) {
            btnSheetDialog.usdBuy.text = ConstVariable.chiselFormat(
                (it.currency!![0].buyValue!!.toBigDecimal()
                    .times(SharedPref.summa.toBigDecimal())).toString()
            )
            btnSheetDialog.usdSell.text =
                ConstVariable.chiselFormat(
                    (it.currency!![0].sellValue!!.toBigDecimal()
                        .times(SharedPref.summa.toBigDecimal())).toString()
                )
            btnSheetDialog.usdCurrency.text = "${SharedPref.summa} ${it.currency!![0].name}"

            btnSheetDialog.rubleBuy.text = ConstVariable.chiselFormat(
                (it.currency!![1].buyValue!!.toBigDecimal()
                    .times(SharedPref.summa.toBigDecimal())).toString()
            )
            btnSheetDialog.rubleSell.text =
                ConstVariable.chiselFormat(
                    (it.currency!![1].sellValue!!.toBigDecimal()
                        .times(SharedPref.summa.toBigDecimal())).toString()
                )
            btnSheetDialog.rubleCurrency.text = "${SharedPref.summa} ${it.currency!![1].name}"


            btnSheetDialog.eurBuy.text = ConstVariable.chiselFormat(
                (it.currency!![2].buyValue!!.toBigDecimal()
                    .times(SharedPref.summa.toBigDecimal())).toString()
            )
            btnSheetDialog.eurSell.text =
                ConstVariable.chiselFormat(
                    (it.currency!![2].sellValue!!.toBigDecimal()
                        .times(SharedPref.summa.toBigDecimal())).toString()
                )
            btnSheetDialog.eurCurrency.text = "${SharedPref.summa} ${it.currency!![2].name}"

        } else {
            btnSheetDialog.apply {
                rubleBuy.text = ConstVariable.chiselFormat(
                    (it.currency!![0].buyValue!!.toBigDecimal()
                        .times(SharedPref.summa.toBigDecimal())).toString()
                )
                rubleSell.text =
                    ConstVariable.chiselFormat(
                        (it.currency!![0].sellValue!!.toBigDecimal()
                            .times(SharedPref.summa.toBigDecimal())).toString()
                    )
                rubleCurrency.text = "${SharedPref.summa} ${it.currency!![0].name}"

                usdBuy.visibility = GONE
                usdCurrency.visibility = GONE
                usdSell.visibility = GONE
                eurBuy.visibility = GONE
                eurCurrency.visibility = GONE
                eurSell.visibility = GONE
                view6.visibility = GONE
                view5.visibility = GONE
            }
        }

        val gradient =
            ConstVariable.getGradient(it.colors!!.firstColor!!, it.colors!!.secondColor!!)

        dialog.findViewById<ConstraintLayout>(R.id.gradientPut)?.background = gradient

        btnSheetDialog.shareButton.setOnClickListener {
            ViewToIntentAsBitmap().outoCreateFile(context, btnSheetDialog.root, "qwertyfilename")
        }

        dialog.show()
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bankName: TextView = itemView.findViewById(R.id.bankName)
        val bankIcon: ImageView = itemView.findViewById(R.id.icon)
        val sellValue: TextView = itemView.findViewById(R.id.sale)
        val buyValue: TextView = itemView.findViewById(R.id.buy)
        val currency: TextView = itemView.findViewById(R.id.currency)
        val menu: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder =
        AdapterViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_recyclerview,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val responseObject = getItem(position)

        Picasso
            .get()
            .load(responseObject.icon)
            .into(holder.bankIcon)

        holder.bankName.text = responseObject.bankName
        holder.buyValue.text = ConstVariable.chiselFormat(
            (responseObject.currency!![number].buyValue!!.toBigDecimal()
                .times(SharedPref.summa.toBigDecimal())).toString()
        )
        holder.sellValue.text =
            ConstVariable.chiselFormat(
                (responseObject.currency!![number].sellValue!!.toBigDecimal()
                    .times(SharedPref.summa.toBigDecimal())).toString()
            )
        holder.currency.text = "${SharedPref.summa} ${responseObject.currency!![number].name}"

        holder.menu.setOnClickListener {
            val popupMenu =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    PopupMenu(holder.itemView.context, it, END, 0, R.style.MyPopupMenu)
                } else {
                    PopupMenu(holder.itemView.context, it)
                }
            popupMenu.inflate(R.menu.recycler_item_menu)

            popupMenu.setOnMenuItemClickListener { menu ->
                if (menu.itemId == R.id.convertItem) {
                    clickItem(getItem(position))
                } else {
                    ViewToIntentAsBitmap().outoCreateFile(holder.itemView.context, holder.itemView, "awfggw")
                }
                true
            }

            popupMenu.show()
        }

        holder.itemView.setOnClickListener {
            clickItem(getItem(position))
        }
    }
}

object RecyclerDiffUtil : DiffUtil.ItemCallback<ResponseObject>() {
    override fun areItemsTheSame(oldItem: ResponseObject, newItem: ResponseObject): Boolean {
        return oldItem.bankName == newItem.bankName
    }

    override fun areContentsTheSame(oldItem: ResponseObject, newItem: ResponseObject): Boolean {
        return oldItem == newItem
    }

}