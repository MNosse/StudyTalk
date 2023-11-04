package br.com.udesc.eso.tcc.studytalk.core.utils

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException

private val FIREBASE_ERROR_MESSAGES = mapOf(
    FirebaseNetworkException::class to "Ocorreu um erro de rede (como tempo limite, conexão interrompida ou host inacessível).",
    FirebaseAuthEmailException::class to "O endereço de e-mail é inválido.",
    FirebaseAuthInvalidCredentialsException::class to "As credenciais fornecidas não são válidas.",
    FirebaseAuthInvalidUserException::class to "A conta do usuário foi desativada pelo administrador.",
    FirebaseAuthWeakPasswordException::class to "A senha fornecida é muito fraca.",
    FirebaseAuthUserCollisionException::class to "Uma conta já existe com o mesmo endereço de e-mail, mas com credenciais diferentes.",
    FirebaseAuthWebException::class to "Ocorreu um erro interno do servidor.",
    FirebaseAuthActionCodeException::class to "O código de ação fornecido não é válido.",
)

private val FIREBASE_AUTH_ERROR_MESSAGES = mapOf(
    "ERROR_INVALID_CUSTOM_TOKEN" to "O token personalizado fornecido não é válido.",
    "ERROR_CUSTOM_TOKEN_MISMATCH" to "O token personalizado não corresponde ao ID do aplicativo ou à chave secreta do projeto.",
    "ERROR_INVALID_CREDENTIAL" to "As credenciais fornecidas não são válidas.",
    "ERROR_INVALID_EMAIL" to "O endereço de e-mail fornecido não é válido.",
    "ERROR_WRONG_PASSWORD" to "A senha fornecida está incorreta.",
    "ERROR_USER_MISMATCH" to "As credenciais fornecidas não correspondem ao usuário anteriormente vinculado.",
    "ERROR_REQUIRES_RECENT_LOGIN" to "A ação requer que o usuário faça login novamente, geralmente por motivos de segurança.",
    "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" to "Uma conta já existe com o mesmo endereço de e-mail, mas com credenciais diferentes.",
    "ERROR_EMAIL_ALREADY_IN_USE" to "O endereço de e-mail fornecido já está em uso por outra conta.",
    "ERROR_CREDENTIAL_ALREADY_IN_USE" to "As credenciais fornecidas já estão vinculadas a outra conta de usuário.",
    "ERROR_USER_DISABLED" to "A conta do usuário foi desativada pelo administrador.",
    "ERROR_USER_NOT_FOUND" to "Não foi encontrada nenhuma conta de usuário correspondente ao endereço de e-mail fornecido.",
    "ERROR_INVALID_USER_TOKEN" to "O token do usuário é inválido, isso pode ocorrer se o usuário se desconectar ou se o token expirar.",
    "ERROR_OPERATION_NOT_ALLOWED" to "A ação que você está tentando realizar não é permitida.",
    "ERROR_WEAK_PASSWORD" to "A senha fornecida é muito fraca.",
    "ERROR_EXPIRED_ACTION_CODE" to "O código de ação (por exemplo, para redefinir a senha) expirou.",
    "ERROR_INVALID_ACTION_CODE" to "O código de ação fornecido não é válido.",
    "ERROR_TOO_MANY_REQUESTS" to "Muitas solicitações foram feitas para a mesma ação nas últimas horas.",
    "ERROR_MISSING_EMAIL" to "O endereço de e-mail é necessário para concluir esta ação.",
    "ERROR_QUOTA_EXCEEDED" to "A cota de envio de e-mails foi excedida.",
    "ERROR_WEB_INTERNAL_ERROR" to "Um erro interno do servidor ocorreu.",
    "ERROR_WEB_STORAGE_UNSUPPORTED" to "O navegador não suporta armazenamento web.",
    "ERROR_WEB_INVALID_POPUP_REDIRECT" to "A URL de redirecionamento da janela pop-up é inválida."
)

fun localizeFirebaseErrorMessages(e: FirebaseException): String? {
    return FIREBASE_ERROR_MESSAGES[e::class]
}

fun localizeFirebaseAuthErrorMessages(errorCode: String): String? {
    return FIREBASE_AUTH_ERROR_MESSAGES[errorCode]
}