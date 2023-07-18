/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.madcamp.treasurehunterar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/** A DialogFragment for the Privacy Notice Dialog Box. */
public class PrivacyNoticeDialogFragment extends DialogFragment {

  /** Listener for weather to start a host or resolve operation. */
  public interface HostResolveListener {

    /** Invoked when the user accepts sharing experience. */
    void onPrivacyNoticeReceived();
  }

  /** Listener for a privacy notice response. */
  public interface NoticeDialogListener {

    /** Invoked when the user accepts sharing experience. */
    void onDialogPositiveClick(DialogFragment dialog);
  }

  NoticeDialogListener noticeDialogListener;
  HostResolveListener hostResolveListener;

  static PrivacyNoticeDialogFragment createDialog(HostResolveListener hostResolveListener) {
    PrivacyNoticeDialogFragment dialogFragment = new PrivacyNoticeDialogFragment();
    dialogFragment.hostResolveListener = hostResolveListener;
    return dialogFragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Verify that the host activity implements the callback interface
    try {
      noticeDialogListener = (NoticeDialogListener) context;
    } catch (ClassCastException e) {
      throw new AssertionError("Must implement NoticeDialogListener", e);
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    noticeDialogListener = null;
    hostResolveListener = null;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
    builder
        .setTitle("카메라 권한이 필요합니다")
        .setMessage("진행하시겠습니까?")
        .setPositiveButton(
            "확인",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                noticeDialogListener.onDialogPositiveClick(PrivacyNoticeDialogFragment.this);
                hostResolveListener.onPrivacyNoticeReceived();
              }
            })
        .setNegativeButton(
            "취소",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                Intent browserIntent =
                    new Intent(
                        Intent.ACTION_VIEW, Uri.parse("5"));
                getActivity().startActivity(browserIntent);
              }
            });
    return builder.create();
  }
}
