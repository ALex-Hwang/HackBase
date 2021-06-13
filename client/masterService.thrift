service masterSvc {
    string createTable(1: string tableName)
    string deleteTable(1: string tableName)
}
