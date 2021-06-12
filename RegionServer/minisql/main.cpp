#include <iostream>
#include "Interpreter.h"
#include "CatalogManager.h"
#include "RecordManager.h"
#include "IndexManager.h"
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "API.h"



void init()
{
	FILE *fp;
	fp = fopen("Indexs", "r");
	if (fp == NULL)
	{
		fp = fopen("Indexs", "w+");
		return;
	}
	fclose(fp);
}

void print()
{
	clock_t finish = clock();
	double duration = (double)(finish - start) / CLOCKS_PER_SEC;
	duration *= 1000;
	printf("now time is %2.1f milliseconds\n", duration * 1000);
}

clock_t start;

int main(int argc, char * argv[])
{
	init();

	API api;
	CatalogManager cm;
	RecordManager rm;

	api.rm = &rm;
	api.cm = &cm;
	IndexManager im(&api);

	api.im = &im;
	rm.api = &api;

	start = 0;
	clock_t finish;

	cout << "*******************Welcome to use our MiniSQL**********************" << endl;
	//cout << "******************* Author: Deng & Xu & Woo **********************" << endl;
	int fileRead = 0;
	//string fileName ="";
	ifstream file,file2;
	Interpreter in;
	in.ap = &api;
	string s;
	int status = 0;
	int filerecovery = 0;
	while (1)
	{
		if (fileRead)
		{

			file.open(in.fileName.c_str());
			if (!file.is_open())
			{
				cout << "Fail to open " << in.fileName << endl;
				fileRead = 0;
				continue;
			}
			while (getline(file, s, ';'))
			{
				in.interpreter(s);
			}
			file.close();
			fileRead = 0;
			finish = clock();
			double duration = (double)(finish - start) / CLOCKS_PER_SEC;
			duration *= 1000;
			printf("%2.1f milliseconds\n", duration);
		}
		else if (filerecovery == 1) {
			ifstream done;
			string donefile = "done.txt";
			string recoverystr = "log.txt";
			string s;
			done.open(donefile.c_str());
			getline(done, s);
			int n = atoi(s.c_str());
			int num = 0;
			int flag = 0;
			done.close();
			file2.open(recoverystr.c_str());
			if (!file2.is_open()) {
				cout << "Fail to open " << recoverystr << endl;
				filerecovery = 0;
				continue;
			}
			while (getline(file2, s)) {
				num++;
				if (num <= n) {
					continue;
				}
				in.interpreter(s);
				flag = 1;
			}
			if (flag == 0) {
				cout << "All actions in log has already been done" << endl;
			}
			filerecovery = 0;
			finish = clock();
			double duration = (double)(finish - start) / CLOCKS_PER_SEC;
			duration *= 1000;
			printf("%2.1f milliseconds\n", duration);
		}
		else
		{

			cout << "minisql>>";
			getline(cin, s, ';');
			start = clock();
			status = in.interpreter(s);
			if (status == 2)
			{
				fileRead = 1;
			}
			else if (status == 3) {
				filerecovery = 1;
			}
			else if (status == 587)
			{
				break;
			}
			else {
				finish = clock();
				double duration = (double)(finish - start) / CLOCKS_PER_SEC;
				duration *= 1000;
				printf("The duration is %2.1f milliseconds\n", duration);
			}
		}

	}

	return 0;
}

