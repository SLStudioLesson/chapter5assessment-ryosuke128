package com.taskapp.dataaccess;

import java.io.*;

import com.taskapp.model.User;

public class UserDataAccess {
    private final String filePath;

    public UserDataAccess() {
        filePath = "app/src/main/resources/users.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public UserDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * メールアドレスとパスワードを基にユーザーデータを探します。
     * @param email メールアドレス
     * @param password パスワード
     * @return 見つかったユーザー
     */
    public User findByEmailAndPassword(String email, String password) {
        /*
         * 1．csvファイルを1行ごとに読み込み「，」で分割
         * 2．上記の情報と、引数で受け取った情報が一致するものを探す
         * 3．一致しなければnullを返す
         */
        User user = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 4) continue;

                String inputEmail = values[2];
                String inputPassword = values[3];
                if (!(email.equals(inputEmail) && password.equals(inputPassword))) continue;

                int code = Integer.parseInt(values[0]);
                String name = values[1];

                user = new User(code, name, inputEmail, inputPassword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * コードを基にユーザーデータを取得します。
     * @param code 取得するユーザーのコード
     * @return 見つかったユーザー
     */
    public User findByCode(int code) {
        /*
         * 1．csvを1行ずつ読み込み「,」で分割する
         * 2．引数で受け取ったcodeと一致する行を探す
         * 3．一致するものがない場合はnuulを返却
         * 4．一致したらUserインスタンスを返却
         */
        User user = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 4) continue;
                int userCode = Integer.parseInt(values[0]);
                if (code != userCode) continue;

                String name = values[1];
                String email = values[2];
                String password = values[3];

                user = new User(userCode, name, email, password);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}
