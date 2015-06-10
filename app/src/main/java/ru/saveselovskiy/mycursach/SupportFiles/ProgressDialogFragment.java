package ru.saveselovskiy.mycursach.SupportFiles;

/**
 * Created by Admin on 10.06.2015.
 */
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.saveselovskiy.mycursach.R;

/**
 * Created by SDK on 15.09.2014.
 */
public class ProgressDialogFragment extends DialogFragment {
    private static String fragmentKey = "dialog";

    private static ProgressDialogFragment newInstance(String message) {
        ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);

        return dialogFragment;
    }

    private static ProgressDialogFragment newInstance(String message, Boolean cancelable) {
        ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(cancelable);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getArguments().getString("message"));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        return dialog;
    }

    public ProgressDialogFragment() {

    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    public static void showProgressDialog(FragmentManager fm, String message) {
        ProgressDialogFragment dialogFragment = (ProgressDialogFragment) fm.findFragmentByTag(fragmentKey);
        if (dialogFragment == null) {
            dialogFragment = ProgressDialogFragment.newInstance(message);
            dialogFragment.show(fm, fragmentKey);
        }
    }

    public static void showProgressDialog(FragmentManager fm, String message, Boolean cancelable) {
        ProgressDialogFragment dialogFragment = (ProgressDialogFragment) fm.findFragmentByTag(fragmentKey);
        if (dialogFragment == null) {
            dialogFragment = ProgressDialogFragment.newInstance(message, cancelable);
            dialogFragment.show(fm, fragmentKey);
        }
    }

    public static void closeProgressDialog(FragmentManager fm, Context context, String message) {
        ProgressDialogFragment dialogFragment = (ProgressDialogFragment) fm.findFragmentByTag(fragmentKey);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
// try {
            if (message != null) {
//                ErrorDialogs.showDialog(context.getString(R.string.error_catched), message, false);
            }
// } catch (Exception e){
// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
// }
        }
    }

    public static void closeProgressDialogSuccessfully(FragmentManager fm) {
        ProgressDialogFragment dialogFragment = (ProgressDialogFragment) fm.findFragmentByTag(fragmentKey);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

}