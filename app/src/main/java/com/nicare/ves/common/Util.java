package com.nicare.ves.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.nicare.ves.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class Util {
    private static final String TAG = "Util";
//    private static String cryptoPass = "sup3rS3xy";

    public static int getIConID(Context context, String icon) {
        return context.getResources().getIdentifier(icon, "drawable", "com.nicare");
    }

    public static String todayDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public static String formatDatToDisplay(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");

        return formatter.format(calendar.getTime());
    }

    public static String dateTimeString() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());

    }

    public String getModel() {
        return Build.BRAND + " " + Build.DEVICE;
    }

    public static Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedByteArray = Base64.decode(base64Str, Base64.NO_WRAP);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        return decodedBitmap;
    }

    public static String encoder(byte byteArray[]) {
        String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedString;
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static ArrayAdapter<String> getSpinnerAdapter(String[] items, Context context) {

        final List<String> plantsList = new ArrayList<>(Arrays.asList(items));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        return spinnerArrayAdapter;

    }

    public static String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] bytes = outputStream.toByteArray();

        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }

    public static void showDialogueMessae(Context context, String msg, String title) {

        AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setIcon(context.getResources().getDrawable(R.drawable.logo))
                .setTitle("Message")
                .setMessage(msg)
                .setPositiveButton("Okay", null)
                .setCancelable(false)
                .create();

        if (!alertDialog.isShowing())
            alertDialog.show();
    }

    public static String toTitleCase(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;

        for (char ch : text.toCharArray()) {

            if (Character.isSpaceChar(ch)) {
                convertNext = true;

            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

//    public static String encryptIt(String value) {
//        try {
//            DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey key = keyFactory.generateSecret(keySpec);
//
//            byte[] clearText = value.getBytes("UTF8");
//            // Cipher is not thread safe
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//
//            String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
////            Log.d(TAG, "Encrypted: " + value + " -> " + encrypedValue);
//            return encrypedValue;
//
//        } catch (InvalidKeyException | InvalidKeySpecException | UnsupportedEncodingException | NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }

    //    public static String decryptIt(String value) {
//        try {
//            DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey key = keyFactory.generateSecret(keySpec);
//
//            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
//            // cipher is not thread safe
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));
//
//            String decrypedValue = new String(decrypedValueBytes);
//            Log.d(TAG, "Decrypted: " + value + " -> " + decrypedValue);
//            return decrypedValue;
//
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }
//    public static final String md5(final String toEncrypt) {
//        try {
//            final MessageDigest digest = MessageDigest.getInstance("md5");
//            digest.update(toEncrypt.getBytes());
//            final byte[] bytes = digest.digest();
//            final StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < bytes.length; i++) {
//                sb.append(String.format("%02X", bytes[i]));
//            }
//            return sb.toString().toLowerCase();
//        } catch (Exception exc) {
//            return ""; // Impossibru!
//        }
//    }
    public static String encryptThisString(String input) {
        try {

            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public static void showToast(String message, boolean isError, Context context, LayoutInflater inflater) {

//        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);
        ImageView ivIcon = layout.findViewById(R.id.ivIcon);
        TextView tvAction = layout.findViewById(R.id.tvAction);

        tvAction.setText(message);

        if (!isError) {
            tvAction.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            tvAction.setTextColor(context.getResources().getColor(R.color.light_red));
        }

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
    public static String formatStat(int count) {
        String coun = String.valueOf(count);
        if (coun.length() == 1) {
            coun = "000" + coun;
        }
        if (coun.length() == 2) {
            coun = "00" + coun;
        }
        if (coun.length() == 3) {
            coun = "0" + coun;
        }
        return coun;
    }
    public static TrustManagerFactory getTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException {
        //Note: hardcode it, because the device might not even have the certificate to download it over https
        String isgCert =
                "-----BEGIN CERTIFICATE-----\n" +
                        "MIIGkTCCBXmgAwIBAgIRAPF7tSmFe2PaVZCZzJCVPLMwDQYJKoZIhvcNAQELBQAw\n" +
                        "cjELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAlRYMRAwDgYDVQQHEwdIb3VzdG9uMRUw\n" +
                        "EwYDVQQKEwxjUGFuZWwsIEluYy4xLTArBgNVBAMTJGNQYW5lbCwgSW5jLiBDZXJ0\n" +
                        "aWZpY2F0aW9uIEF1dGhvcml0eTAeFw0yMzAxMjQwMDAwMDBaFw0yMzA0MjQyMzU5\n" +
                        "NTlaMBsxGTAXBgNVBAMTEG5nc2NoYS5uaS5nb3YubmcwggEiMA0GCSqGSIb3DQEB\n" +
                        "AQUAA4IBDwAwggEKAoIBAQCiIC3we2UQh5sWplOVdpjdYE8pFlFy0r0CrtuL3rbM\n" +
                        "V0S1QrcdTvTdtSKfetw6XayMkme+ag3o0orsI3W+9/R+dvPwJHVn9O4ytg8GZCKH\n" +
                        "lcmaKMWmiLFH+dLqanPRsf/l61H7ER2na2DZKtbul6WlSuUJONziX98iZevi/RwY\n" +
                        "63rPGQQKJeh9jVfATuC/KIxM123GbnTswQ3oBozbx04A4rYSa8ccRupn8flw5r02\n" +
                        "u7MnrqYxK7Ou9uqeviEQMqLiyXql6GctfqG5zA2OIPRMk2N0rtzRFpsiJg5zJtTh\n" +
                        "pdnl/6U24dPVrtY7FeMHg60WlX5CkLurbJ2gN1GMq3GlAgMBAAGjggN3MIIDczAf\n" +
                        "BgNVHSMEGDAWgBR+A1plQWunfgrhuJ0I6h2OHWrHZTAdBgNVHQ4EFgQUMVN6THNZ\n" +
                        "H3/WV9K9Su0F3/wUbW0wDgYDVR0PAQH/BAQDAgWgMAwGA1UdEwEB/wQCMAAwHQYD\n" +
                        "VR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMEkGA1UdIARCMEAwNAYLKwYBBAGy\n" +
                        "MQECAjQwJTAjBggrBgEFBQcCARYXaHR0cHM6Ly9zZWN0aWdvLmNvbS9DUFMwCAYG\n" +
                        "Z4EMAQIBMEwGA1UdHwRFMEMwQaA/oD2GO2h0dHA6Ly9jcmwuY29tb2RvY2EuY29t\n" +
                        "L2NQYW5lbEluY0NlcnRpZmljYXRpb25BdXRob3JpdHkuY3JsMH0GCCsGAQUFBwEB\n" +
                        "BHEwbzBHBggrBgEFBQcwAoY7aHR0cDovL2NydC5jb21vZG9jYS5jb20vY1BhbmVs\n" +
                        "SW5jQ2VydGlmaWNhdGlvbkF1dGhvcml0eS5jcnQwJAYIKwYBBQUHMAGGGGh0dHA6\n" +
                        "Ly9vY3NwLmNvbW9kb2NhLmNvbTCCAQUGCisGAQQB1nkCBAIEgfYEgfMA8QB2AK33\n" +
                        "vvp8/xDIi509nB4+GGq0Zyldz7EMJMqFhjTr3IKKAAABheRL8IkAAAQDAEcwRQIg\n" +
                        "HyLQ89yn5KufdiuEGDgD6Bd6GqqaLrvfCVggR3u3coYCIQDGE8m/FZQtQwAuMeY9\n" +
                        "u7To4TgFmkN9qaG/UZGsnHQJjQB3AHoyjFTYty22IOo44FIe6YQWcDIThU070ivB\n" +
                        "OlejUutSAAABheRL8G0AAAQDAEgwRgIhAI0JbI2XoGdXRBmuReKPvlJoTWZllknE\n" +
                        "O/z6tSQOqykZAiEA/rwtnJ3Fa/ADNuNEaMhlBJGPkcATYFXFrj2TbKy3TW8wgdIG\n" +
                        "A1UdEQSByjCBx4IQbmdzY2hhLm5pLmdvdi5uZ4IXY3BhbmVsLm5nc2NoYS5uaS5n\n" +
                        "b3YubmeCHGNwY2FsZW5kYXJzLm5nc2NoYS5uaS5nb3YubmeCG2NwY29udGFjdHMu\n" +
                        "bmdzY2hhLm5pLmdvdi5uZ4IVbWFpbC5uZ3NjaGEubmkuZ292Lm5nghh3ZWJkaXNr\n" +
                        "Lm5nc2NoYS5uaS5nb3YubmeCGHdlYm1haWwubmdzY2hhLm5pLmdvdi5uZ4IUd3d3\n" +
                        "Lm5nc2NoYS5uaS5nb3YubmcwDQYJKoZIhvcNAQELBQADggEBAE71lZeehlSc7zBc\n" +
                        "9AS1aBhPyV1hVx0TU2UNtS67Ib7SkguLmZj1fh8ScjJfxU+3ydqHnwzQXJtAZRAz\n" +
                        "4KTTaJs6rhn+SXVzV8vcVyBvIUXq0Vi0yxt4S39Yc7zt/DQqkoI73/gmVaGyRhyq\n" +
                        "wgT9koxO3H8A1CDOsn8G9nbkhihsoETH1fmLPfIlsvWEZ0fvKL1XC3Oi3MvvQLzh\n" +
                        "7dQ/WC0KCiO9Jkodyc8iUTbDCVjKpOhcsJVbtjWXEmjw1pfKwj8uKzzY/e4SEB5B\n" +
                        "IO6TixEu4nMHr3eDXswjxeuk7rXza2Mf8Op6hKyssL/BYN0ju8xzwL56mmEkPWMc\n" +
                        "QxHEsIQ=\n" +
                        "-----END CERTIFICATE-----";

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate isgCertificate = cf.generateCertificate(new ByteArrayInputStream(isgCert.getBytes(StandardCharsets.UTF_8)));

        // Create a KeyStore containing our trusted CAs
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("isrg_root", isgCertificate);

        //Default TrustManager to get device trusted CA
        TrustManagerFactory defaultTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        defaultTmf.init((KeyStore) null);

        X509TrustManager trustManager = (X509TrustManager) defaultTmf.getTrustManagers()[0];
        int number = 0;
        for(Certificate cert : trustManager.getAcceptedIssuers()) {
            keyStore.setCertificateEntry(Integer.toString(number), cert);
            number++;
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        return tmf;
    }

}
