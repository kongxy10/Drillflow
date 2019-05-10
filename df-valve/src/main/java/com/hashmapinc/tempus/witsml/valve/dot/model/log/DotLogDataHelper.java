/**
 * Copyright © 2018-2019 Hashmap, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hashmapinc.tempus.witsml.valve.dot.model.log;

import java.util.Arrays;
import java.util.List;

import com.hashmapinc.tempus.WitsmlObjects.Util.log.LogDataHelper;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsLogCurveInfo;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsLogData;


public class DotLogDataHelper extends LogDataHelper {

    public static String convertDataToWitsml20From1311(com.hashmapinc.tempus.WitsmlObjects.v1311.ObjLog log){
        if (log == null)
            return null;

        return convertStringListToJson(log.getLogData().getData());

    }

    private static String convertDataToWitsml20From1411(com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLog log){
        if (log == null)
            return null;

        return convertStringListToJson(log.getLogData().get(0).getData());
    }

    private static String convertStringListToJson(List<String> dataLines){
        String wml20Data = "[";
        for (int j = 0; j < dataLines.size(); j++){
            List<String> data = Arrays.asList(dataLines.get(j).split("\\s*,\\s*"));
            for (int i = 0; i < data.size(); i++){
                if (i == 0){
                    wml20Data = wml20Data + "[[" + data.get(i) + "]";
                } else if (i == 1){
                    wml20Data = wml20Data + ",[" + data.get(i);
                } else {
                    wml20Data = wml20Data + ", "+ data.get(i);
                }
                if (i == (data.size() - 1)){
                    wml20Data = wml20Data + "]]";
                    if (j != (dataLines.size() - 1)){
                        wml20Data = wml20Data + ",";
                    }
                }
            }
        }
        wml20Data = wml20Data + "]";
        return wml20Data;
    }

    public static String convertDataToDotFrom1411(com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLog log){
        String result = "{";
        result = result + "\"mnemonicList\":" + "\"" + log.getLogData().get(0).getMnemonicList() + "\"" + ",";
        result = result + "\"data\":" + "\"" + convertDataToWitsml20From1411(log) + "\"}";
        return result;
    }

    public static String convertDataToDotFrom1311(com.hashmapinc.tempus.WitsmlObjects.v1311.ObjLog log){
        String result = "{";
        String mnemList="";
        for (int i = 0; i < log.getLogCurveInfo().size(); i++){
            mnemList = mnemList + log.getLogCurveInfo().get(i).getMnemonic();
            if ((i + 1) < log.getLogCurveInfo().size())
                mnemList = mnemList + ",";
        }
        result = result + "\"mnemonicList\":" + "\"" + mnemList + "\"" + ",";
        result = result + "\"data\":" + "\"" + convertDataToWitsml20From1311(log) + "\"}";
        return result;
    }
}