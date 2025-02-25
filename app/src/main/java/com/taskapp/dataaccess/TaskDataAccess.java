package com.taskapp.dataaccess;

import java.util.*;

import com.taskapp.model.Log;
import com.taskapp.model.Task;
import com.taskapp.model.User;
import com.taskapp.model.Task;

import java.io.*;

public class TaskDataAccess {

    private final String filePath;

    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        /*
         * 1．csvを1行ずつ読み込いみ「,」で分割
         * 2．分割した情報からTaskクラスをインスタンス化
         * 3．インスタンス化したものをListへ追加
         */

        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 4) continue;

                int code = Integer.parseInt(values[0]);
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                User user = userDataAccess.findByCode(Integer.parseInt(values[3]));

                Task task = new Task(code, name, status, user);
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        /*
         * 1．受け取ったtaskをcreateLineに渡しフォーマットを作成
         * 2．作成したフォーマットをcsvに追記
         */
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = createLine(task);
            writer.newLine();
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    public Task findByCode(int code) {
        /*
         * 1．csvを1行ずつ読み込み「,」で分割する
         * 2．引数で受け取ったcodeと一致する行を探す
         * 3．一致するものがない場合はnullを返却
         * 4．一致したらUserインスタンスを返却
         */
        Task task = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int taskCode = Integer.parseInt(values[0]);
                if (taskCode != code) continue;

                String name = values[1];
                int status = Integer.parseInt(values[2]);
                User user = userDataAccess.findByCode(Integer.parseInt(values[3]));

                task = new Task(taskCode, name, status, user);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        /*
         * 1．findAllメソッドからListを受け取る
         * 2．Listをもとにcsvに書き込みを行う
         * 3．書きこむTaskCodeとupdateTaskCodeが一致した場合はupdateTaskの情報を書き込む
         */
        List<Task> tasks = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(Code,Name,Status,Rep_User_Code);
            writer.newLine();
            
            String line;
            for (Task task : tasks) {
                if (task.getCode() == updateTask.getCode()) {
                    line = createLine(updateTask);
                } else {
                    line = createLine(task);
                }

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    public void delete(int code) {
        /*
         * 1．findAllからListを受け取る
         * 2．Listの情報からcsvに上書き。codeと一致する情報は記載しない
         */
        List<Task> tasks = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            String line;
            writer.write("Code,Name,Status,Rep_User_Code");
            writer.newLine();

            for (Task task : tasks) {
                if (task.getCode() == code) continue;
                line = createLine(task);
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    private String createLine(Task task) {
        return task.getCode() + "," + task.getName() + "," + task.getStatus() + "," + task.getRepUser().getCode();
    }
}