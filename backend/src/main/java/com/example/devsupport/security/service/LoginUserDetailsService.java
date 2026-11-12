package com.example.devsupport.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.devsupport.security.principal.LoginUserDetails;
import com.example.devsupport.user.repository.UserRepository;

// LoginUserDetailsServiceは、Spring SecurityのUserDetailsServiceインターフェースを実装し、ユーザー認証のためにユーザー情報を提供するサービスクラス
@Service
public class LoginUserDetailsService implements UserDetailsService {
    // コンストラクタで受け取るUserRepositoryを保持する。finalにより初期化後の参照の再代入を禁止する
    private final UserRepository userRepository;

    // コンストラクタでUserRepositoryを受け取り定義する。コンストラクタが1つなのでAutowiredを付けずに注入できる
    public LoginUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    // メールアドレスを使ってユーザー検索し、LoginUserDetailsに変換して返す
    // 見つからない場合はUsernameNotFoundExceptionをThrowする
    public LoginUserDetails loadUserByUsername(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .map(user -> new LoginUserDetails(
                        user.getId(),
                        user.getEmail(),
                        user.getPasswordHash(),
                        user.getSystemRole().getCode()))
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません。"));
    }
}
