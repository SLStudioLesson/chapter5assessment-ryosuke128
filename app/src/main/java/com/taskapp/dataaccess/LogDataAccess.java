package com.taskapp.dataaccess;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import com.taskapp.model.*;

public class LogDataAccess {
    private final String filePath;


    public LogDataAccess() {
        filePath = "app/src/main/resources/logs.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public LogDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * ログをCSVファイルに保存します。
     *
     * @param log 保存するログ
     */
    public void save(Log log) {
        /*
         * 1．受け取ったlogをcreateLineに渡しフォーマットを作成
         * 2．作成したフォーマットをcsvに追記
         */
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = createLine(log);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
    public List<Log> findAll() {
        /*
         * 1．csvを1行ずつ読み込いみ「,」で分割
         * 2．分割した情報からTaskクラスをインスタンス化
         * 3．インスタンス化したものをListへ追加
         */
        List<Log> logs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 4) continue;

                int taskCode = Integer.parseInt(values[0]);
                int changeUserCode = Integer.parseInt(values[1]);
                int status = Integer.parseInt(values[2]);
                LocalDate changeDate = values[3];

                Log log = new Log(taskCode, changeUserCode, status, changeDate);
                logs.add(Log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @see #findAll()
     * @param taskCode 削除するログのタスクコード
     */
    public void deleteByTaskCode(int taskCode) {
        /*
         * 1．findAllからListを受け取る
         * 2．Listの情報でcsvに上書き。taskCodeと一致するものは記載しない
         */
        List<Log> logs = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            String line;
            writer.write("Task_Code,Change_User_Code,Status,Change_Date");
            writer.newLine();

            for (Log log : logs) {
                if (log.getTaskCode() == taskCode) continue;
                line = createLine(log);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ログをCSVファイルに書き込むためのフォーマットを作成します。
     *
     * @param log フォーマットを作成するログ
     * @return CSVファイルに書き込むためのフォーマット
     */
    private String createLine(Log log) {
        return log.getTaskCode() + "," + log.getChangeUserCode() + "," + log.getStatus() + "," + log.getChangeDate().toString(); 
    }

}