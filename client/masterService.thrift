service masterSvc {
    string createTable(1: string tableName)
    bool deleteTable(1: string tableName)
}
