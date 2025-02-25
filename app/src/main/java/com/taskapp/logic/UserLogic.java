package com.taskapp.logic;

import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.User;

public class UserLogic {
    private final UserDataAccess userDataAccess;

    public UserLogic() {
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param userDataAccess
     */
    public UserLogic(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    /**
     * ユーザーのログイン処理を行います。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByEmailAndPassword(String, String)
     * @param email ユーザーのメールアドレス
     * @param password ユーザーのパスワード
     * @return ログインしたユーザーの情報
     * @throws AppException メールアドレスとパスワードが一致するユーザーが存在しない場合にスローされます
     */
    public User login(String email, String password) throws AppException {
        /*
         * 1．Userインスタンスを作成
         * 2．UserDataAccessクラスのfindByEmailAndPasswordを呼び出しUserインスタンスに代入
         * 3．nullが帰ってきた場合は例外をスロー
         * 4．ログインできた場合はメッセージを出力
         * 5．呼び出しもとにUserインスタンスを返す
         */

        User user = userDataAccess.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new AppException("既に登録されているメールアドレス、パスワードを入力してください");
        }

        System.out.println("ユーザー名：" + user.getName() + "でログインしました。");
        return user;
    }
}