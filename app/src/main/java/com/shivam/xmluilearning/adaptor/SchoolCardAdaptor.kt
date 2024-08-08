package com.shivam.xmluilearning.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.shivam.xmluilearning.HomeFragmentDirections
import com.shivam.xmluilearning.R
import com.shivam.xmluilearning.databinding.LayoutSchoolDetailsBinding
import com.shivam.xmluilearning.model.School

class SchoolCardAdaptor( private val schools: List<School>): RecyclerView.Adapter<SchoolCardAdaptor.ViewHolder>() {
    class ViewHolder( val binding: LayoutSchoolDetailsBinding):RecyclerView.ViewHolder(binding.root) {

    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("Not yet implemented")
        return ViewHolder(LayoutSchoolDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return schools.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("Not yet implemented")

        val currentItem = schools[position]
        holder.apply {

            binding.title.text = currentItem.name
            binding.location.text = currentItem.address
            binding.rating.text = currentItem.ratings
            binding.ratingCount.text = currentItem.ratingCount
            binding.idSchoolCardContainer.setOnClickListener {

                val action = HomeFragmentDirections.actionHomeFragmentToFragmentUpdateData(schools[position].id!!)

                findNavController(holder.itemView).navigate(action)
            }

            binding.idSchoolCardContainer.setOnLongClickListener {


                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("delete item permamentaly ")
                    .setMessage("R u sure?")
                    .setPositiveButton("yes"){  _,_ ->
                        val firebaseRef = FirebaseDatabase.getInstance().getReference("schools list")
                        firebaseRef.child(currentItem.id.toString()).removeValue()
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(holder.itemView.context, "item deleted successfully", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnCanceledListener{
                                Toast.makeText(holder.itemView.context, "error $it", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                    .setNegativeButton("NO"){_,_ ->
                        Toast.makeText(holder.itemView.context, "item not deleted", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            return@setOnLongClickListener true
            }
        }
    }
}