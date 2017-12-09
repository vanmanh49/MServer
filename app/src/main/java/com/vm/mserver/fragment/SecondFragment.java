package com.vm.mserver.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vm.mserver.R;
import com.vm.mserver.dao.CRUD;
import com.vm.mserver.model.Student;

import java.util.List;

/**
 * Created by VanManh on 08-Dec-17.
 */

public class SecondFragment extends Fragment {

    private List<Student> studentList;
    private ArrayAdapter adapter;
    private CRUD crud;
    private static SecondFragment fragment;

    public SecondFragment() {
    }

    public static SecondFragment getInstance() {
        if (fragment == null) {
            fragment = new SecondFragment();
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
        studentList = crud.getAllStudents();
        adapter = new ArrayAdapter(context, R.layout.item_student, studentList);
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
        menu.add(1, 0, 0, "Sửa");
        menu.add(1, 1, 1, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getGroupId() == 1) {
            switch (item.getItemId()) {
                case 0:
                    showEditDialog(info.position, info.targetView.getContext());
                    return true;
                case 1:
                    showDeleteConfirmDialog(studentList.get(info.position), info.targetView.getContext());
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteConfirmDialog(final Student student, final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa")
                .setMessage("Bạn có muốn xóa " + student.getName() + " không?")
                .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (crud.deleteStudent(student)) {
                            studentList.remove(student);
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
        final Student s = studentList.get(pos);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diaglog_add_edit_student, null);
        EditText edtMaSo = dialogView.findViewById(R.id.ms);
        final EditText edtName = dialogView.findViewById(R.id.name);
        final EditText edtToan = dialogView.findViewById(R.id.toan);
        final EditText edtLy = dialogView.findViewById(R.id.ly);
        final EditText edtHoa = dialogView.findViewById(R.id.hoa);
        edtMaSo.setText(s.getMs() + "");
        edtMaSo.setEnabled(false);
        edtName.setText(s.getName());
        edtToan.setText(s.getToan() + "");
        edtLy.setText(s.getLy() + "");
        edtHoa.setText(s.getHoa() + "");

        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_title_update_info)
                .setMessage(R.string.dialog_edit_mess)
                .setView(dialogView)
                .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name1 = edtName.getText().toString();
                        Double toan = Double.valueOf(edtToan.getText().toString());
                        Double ly = Double.valueOf(edtLy.getText().toString());
                        Double hoa = Double.valueOf(edtHoa.getText().toString());
                        Student student = new Student(s.getMs(), name1, toan, ly, hoa);
                        if (student.checkValidate()) {
                            boolean rs = crud.updateStudent(student);
                            if (rs) {
                                int vt = studentList.indexOf(s);
                                studentList.remove(vt);
                                studentList.add(vt, student);
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
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diaglog_add_edit_student, null);
        final EditText edtMaSo = dialogView.findViewById(R.id.ms);
        final EditText edtName = dialogView.findViewById(R.id.name);
        final EditText edtToan = dialogView.findViewById(R.id.toan);
        final EditText edtLy = dialogView.findViewById(R.id.ly);
        final EditText edtHoa = dialogView.findViewById(R.id.hoa);

        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_student_title)
                .setView(dialogView)
                .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int ms = Integer.valueOf(edtMaSo.getText().toString());
                        String name1 = edtName.getText().toString();
                        Double toan = Double.valueOf(edtToan.getText().toString());
                        Double ly = Double.valueOf(edtLy.getText().toString());
                        Double hoa = Double.valueOf(edtHoa.getText().toString());
                        Student student = new Student(ms, name1, toan, ly, hoa);
                        if (student.checkValidate()) {
                            boolean rs = crud.addNewStudent(student);
                            if (rs) {
                                studentList.add(student);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Dữ liệu lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }
}
