package com.bonny.bonnyparent.util;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class Utils {
    private static byte[] tag;

    public static byte[] getTag() {
        return tag;
    }

    public void setTag(byte[] tag) {
        this.tag = tag;
    }

    public static String getFormattedBloodGroup(String blood_group) {
        switch (blood_group) {
            case "a_positive":
                return "A Positive";

            case "a_negative":
                return "A Negative";

            case "b_positive":
                return "B Positive";

            case "b_negative":
                return "B Negative";

            case "o_positive":
                return "O Positive";

            case "o_negative":
                return "O Negative";

            case "ab_positive":
                return "AB Positive";

            case "ab_negative":
                return "AB Negative";
        }

        return null;
    }

    public static String getRawBloodGroup(String blood_group) {
        switch (blood_group) {
            case "A Positive":
                return "a_positive";

            case "A Negative":
                return "a_negative";

            case "B Positive":
                return "b_positive";

            case "B Negative":
                return "b_negative";

            case "O Positive":
                return "o_positive";

            case "O Negative":
                return "o_negative";

            case "AB Positive":
                return "ab_positive";

            case "AB Negative":
                return "ab_negative";
        }

        return null;
    }

    public static String getDosage(String dosage) {
        switch (dosage) {
            case "bcg":
                return "BCG";

            case "opv":
                return "OPV0";

            case "hepb1":
                return "HEP-B 1";

            case "dt1":
                return "DTwP 1";

            case "ipv1":
                return "IPV 1";

            case "hepb2":
                return "HEP-B 2";

            case "hib1":
                return "HIB 1";

            case "rota1":
                return "Rotavirus 1";

            case "pcv1":
                return "PCV 1";

            case "dt2":
                return "DTwP 2";

            case "ipv2":
                return "IPV 2";

            case "hib2":
                return "HIB 2";

            case "rota2":
                return "Rotavirus 2";

            case "pcv2":
                return "PCV 2";

            case "dt3":
                return "DTwP 3";

            case "ipv3":
                return "IPV 3";

            case "hib3":
                return "HIB 3";

            case "rota3":
                return "Rotavirus 3";

            case "pcv3":
                return "PCV 3";

            case "opv1":
                return "OPV 1";

            case "hepb3":
                return "HEP-B 3";

            case "opv2":
                return "OPV 2";

            case "mmr1":
                return "MMR-1";

        }
        return null;
    }

    public static String getRawVaccine(String blood_group) {
        switch (blood_group) {
            case "BCG":
                return "bcg";

            case "OPV0":
                return "opv";

            case "HEP-B 1":
                return "hepb1";

            case "DTwP 1":
                return "dt1";

            case "IPV 1":
                return "ipv1";

            case "HEP-B 2":
                return "hepb2";

            case "HIB 1":
                return "hib1";

            case "Rotavirus 1":
                return "rota1";

            case "PCV 1":
                return "pcv1";

            case "DTwP 2":
                return "dt2";

            case "IPV 2":
                return "ipv2";

            case "HIB 2":
                return "hib2";

            case "Rotavirus 2":
                return "rota2";

            case "PCV 2":
                return "pcv2";

            case "DTwP 3":
                return "dt3";

            case "IPV 3":
                return "ipv3";

            case "HIB 3":
                return "hib3";

            case "Rotavirus 3":
                return "rota3";

            case "PCV 3":
                return "pcv3";

            case "OPV 1":
                return "opv1";

            case "HEP-B 3":
                return "hepb3";

            case "OPV 2":
                return "opv2";

            case "MMR-1":
                return "mmr1";
        }
        return null;
    }

    public static ArrayAdapter<String> pendingVaccineAdapter(Context context) {
        ArrayAdapter arrayAdapter;
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Select");
        strings.add("bcg");
        strings.add("opv");
        strings.add("hepb1");
        strings.add("dt1");
        strings.add("ipv1");
        strings.add("hepb2");
        strings.add("hib1");
        strings.add("rota1");
        strings.add("pcv1");
        strings.add("dt2");
        strings.add("ipv2");
        strings.add("hib2");
        strings.add("rota2");
        strings.add("pcv2");
        strings.add("dt3");
        strings.add("ipv3");
        strings.add("hib3");
        strings.add("rota3");
        strings.add("pcv3");
        strings.add("opv1");
        strings.add("hepb3");
        strings.add("opv2");
        strings.add("mmr1");
        arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, strings);
        return arrayAdapter;
    }
}
