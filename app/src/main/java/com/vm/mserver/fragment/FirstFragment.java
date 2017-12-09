package com.vm.mserver.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.vm.mserver.R;
import com.vm.mserver.dao.CRUD;
import com.vm.mserver.model.Contact;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by VanManh on 08-Dec-17.
 */

public class FirstFragment extends Fragment {

    private List<Contact> contactList;
    private BaseAdapter adapter;
    private CRUD crud;
    private static FirstFragment fragment;

    public FirstFragment() {
    }

    public static FirstFragment getInstance() {
        if (fragment == null) {
            fragment = new FirstFragment();
        }
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main_fragment, container, false);
        Context context = container.getContext();
        final ListView listView = view.findViewById(R.id.list_view);
        crud = CRUD.getInstants(context);
        contactList = crud.getRegisterContact();
        adapter = new FirstAdapter(contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().openContextMenu(view);
            }
        });
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Chọn chức năng");
        menu.add(0, 0, 0, "Sửa");
        menu.add(0, 1, 1, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId() == 0) {
            switch (item.getItemId()) {
                case 0:
                    showEditDialog(info.position, info.targetView.getContext());
                    return true;
                case 1:
                    showDeleteConfirmDialog(contactList.get(info.position), info.targetView.getContext());
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteConfirmDialog(final Contact contact, final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa")
                .setMessage("Bạn có muốn xóa " + contact.getName() + " không?")
                .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (crud.deleteContact(contact)) {
                            contactList.remove(contact);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showEditDialog(int pos, final Context context) {
        final Contact oldContact = contactList.get(pos);
        LayoutInflater inflater = getLayoutInflater();
        View container = inflater.inflate(R.layout.dialog_add_edit_contact, null);
        final EditText edtName = container.findViewById(R.id.edt_c_name);
        final EditText edtPhoneNum = container.findViewById(R.id.edt_c_phone_num);
        edtName.setText(oldContact.getName());
        edtPhoneNum.setText(oldContact.getPhoneNum());
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_title_update_info)
                .setMessage(R.string.edt_add_phone_mess)
                .setView(container)
                .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = edtName.getText().toString();
                        String phoneNum = edtPhoneNum.getText().toString();
                        Contact contact = new Contact(oldContact.getId(), name, phoneNum);
                        if (contact.checkValidate()) {
                            boolean rs = crud.updateContact(contact);
                            if (rs) {
                                int vt = contactList.indexOf(oldContact);
                                contactList.remove(vt);
                                contactList.add(vt, contact);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Dữ liệu lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    public void showInputDialog(final Context context) {
        LayoutInflater inflater = getLayoutInflater();
        View container = inflater.inflate(R.layout.dialog_add_edit_contact, null);
        final EditText edtName = container.findViewById(R.id.edt_c_name);
        final EditText edtPhoneNum = container.findViewById(R.id.edt_c_phone_num);
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_contact_title)
                .setView(container)
                .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = edtName.getText().toString();
                        String phoneNum = edtPhoneNum.getText().toString();
                        Contact contact = new Contact(name, phoneNum);
                        if (contact.checkValidate()) {
                            if (!crud.isPhoneNumExist(contact.getPhoneNum())) {
                                boolean rs = crud.registerContact(contact);
                                if (rs) {
                                    contactList.add(contact);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Thêm thất bại!\nSố điện thoại đã được sử dụng!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Dữ liệu lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

}
