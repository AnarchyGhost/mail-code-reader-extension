export interface AccountModel {
    name: string,
    host: string,
    port: number,
    username: string,
    password: string,
    useSsl: boolean,
    titleRegex: string,
    codeRegex: string,
    folder: string,
    searchInBody: boolean,
    searchCount: number,
}

export interface SettingsModel {
    url: string,
    accounts: Array<AccountModel>
}