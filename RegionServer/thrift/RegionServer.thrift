service regionRPC {
  string send(1: string command);
  string recover(1: string log); // could be the log content or the path to the log
}
