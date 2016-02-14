# Owen's Automated Attendance Taker
# author: Owen Tian
# date: 1/5/2015

import csv
import shutil
import os

'''

at the beginning of a new cycle:

- run make_new_master() using master attendance from previous cycle
- archive old copy files
- change DATES
- change BEGINNER_SONG, INTERMEDIATE_SONG, ADVANCED_SONG
- run make_master_attendance() and make_song_attendance() to create 4 attendance files
- copy-paste the contents of new_master.csv into the new MASTER.csv file


standard attendance instructions:

- change SESSION_NUM to appropriate number to grab correct file name
- change INDEX to the correct column value (should be same as SESSION_NUM!)
- run take_attendance() for each song
- run update_master()
- run make_copies() to create a session-specific folder that holds old copies of attendance


'''

BEGINNER_SONG = "trap"
INTERMEDIATE_SONG = "blacklist"
ADVANCED_SONG = "delicious"
SESSION_NUM = "_session1.txt"

# index for column we are taking attendance in (for session 1, this is 1.
# for 2, it is 2, etc.)
INDEX = 1

# file names of attendance sheets to be read in (IN stands for "read-in")
BEGINNER_IN = BEGINNER_SONG + SESSION_NUM
INTERMEDIATE_IN = INTERMEDIATE_SONG + SESSION_NUM
ADVANCED_IN = ADVANCED_SONG + SESSION_NUM

# file names of song attendance sheets
BEG_ATTEND = "MASTER_" + BEGINNER_SONG + ".csv"
INT_ATTEND = "MASTER_" + INTERMEDIATE_SONG + ".csv"
ADV_ATTEND = "MASTER_" + ADVANCED_SONG + ".csv"
MASTER = "MASTER.csv"

# header for song attendance sheets and master attendance
DATES = ["10-29", "11-1", "11-2", "11-5", "11-8", \
      "11-9", "11-12", "11-15"]
HEADER = ",".join(["NAME"] + DATES)
MASTER_HEADER = ",".join(["NAME", "PAID"] + DATES + ["CARRYOVER"])

# helper function that reads in a file and makes a list from its rows
def make_list(file):
    infile = open(file, "r")

    attend_list = []
    for line in infile:
        if line[-1] == "\n":
            attend_list.append(line[:-1])
        else:
            attend_list.append(line)

    infile.close()

    # print("attend_list is " + str(attend_list))
    return attend_list


# create song attendance sheets with headers
def make_song_attendance(beg, inter, adv):
    if not os.path.exists(beg):
        print("creating beginner attendance file")
        beginner = open(beg, "w")
        print(HEADER, file=beginner)
        beginner.close()

    if not os.path.exists(inter):
        print("creating intermediate attendance file")
        intermediate = open(inter, "w")
        print(HEADER, file=intermediate)
        intermediate.close()

    if not os.path.exists(adv):
        print("creating advanced attendance file")
        advanced = open(adv, "w")
        print(HEADER, file=advanced)
        advanced.close()

# create master attendance sheet with header
def make_master_attendance(mast):
    if not os.path.exists(mast):
        print("creating master attendance list")
        master = open(mast, "w")
        print(MASTER_HEADER, file=master)
        master.close()

# function that take file of names and updates the song attendance sheet  
def take_attendance(attendance, master):
    print("-------taking attendance for " + str(attendance) + "-------")
    attend_list = make_list(attendance)

    # find the matches between attend_list and the master attendance
    with open(master, 'rt') as f:
        # found keeps track of if we have made a match
        found = False
        
        read_master = csv.reader(f)
        master_list = list(read_master)
        # print(master_list)
        for person in attend_list:
            for row in master_list:
                if person == row[0]:
                    found = True
                    print("match found: " + str(person))
                    row[INDEX] = 1
            if not found:
                new = [person, '0', '0', '0', '0', '0', '0', '0', '0']
                new[INDEX] = 1
                print("new attendee: " + str(person))
                master_list.append(new)
            found = False
        # print(master_list)

        # writing the results saved in 'master_list' to the attendance file
        with open(master, 'w', newline='') as outfile:
            writer = csv.writer(outfile)
            writer.writerows(master_list)

# function that updates the master attendance sheet with everyone
# who attended a session
def update_master(beg, inter, adv, master):
    print("-------updating master attendance-------")
    # compile a complete list of everyone who went to a session
    attend_list = []
    b_list = make_list(beg)
    i_list = make_list(inter)
    a_list = make_list(adv)
    
    attend_list.extend(b_list)
    attend_list.extend(i_list)
    attend_list.extend(a_list)
    # removes duplicates
    attend_list = set(attend_list)

    # print("complete list of attendees for master is " + str(attend_list))

    with open(master, 'rt') as f:
        read_master_attend = csv.reader(f)
        master_attendance = list(read_master_attend)

        found = False
        # check for matches between the complete list of people who attended
        # and the master_attendance + update it
        for person in attend_list:
            for row in master_attendance:
                if person == row[0]:
                    found = True
                    print("match found: " + str(person))
                    row[INDEX + 1] = 1
            if not found:
                new = [person, 'N/A', '0', '0', '0', '0', '0', '0', '0', '0', '0']
                new[INDEX + 1] = 1
                print("new attendee: " + str(person))
                master_attendance.append(new)
            found = False
            
        # go through entire master attendance, update paid
        attend_count = 0
        for row in master_attendance[1:]:
            for i in range(2,11):
                attend_count = attend_count + int(row[i])
            if attend_count >= 3 and row[1] != "Paid":
                # print("need to pay: " + str(row[0]))
                row[1] = "NEED TO PAY"
            attend_count = 0

        # write results in master_attendance to the master attendance file
        with open(master, 'w', newline='') as outfile:
            writer = csv.writer(outfile)
            writer.writerows(master_attendance)       

# function to insert people who have already paid into master attendance
# if they exist already, update them to paid
def update_paid(people, master):
    print("----------updating paid----------")
    paid = make_list(people)

    print("paid is " + str(paid))

    with open(master, 'rt') as f:
        read_master_attend = csv.reader(f)
        master_attendance = list(read_master_attend)

        found = False
        for person in paid:
            for row in master_attendance:
                if person == row[0]:
                    found = True
                    print(str(person) + " already exists in master. updating to paid.")
                    row[1] = "Paid"
            if not found:
                new = [person, 'Paid', '0', '0', '0', '0', '0', '0', '0', '0', '0']
                print("creating person who has paid in master: " + str(person))
                master_attendance.append(new)
            found = False

        # write results to master attendance file
        with open(master, 'w', newline='') as outfile:
            writer = csv.writer(outfile)
            writer.writerows(master_attendance)     


# function to find all people who need to pay
def need_pay(master):
    hunt_down = []
    with open(master, 'rt') as f:
        read_master_attend = csv.reader(f)
        master_attendance = list(read_master_attend)

        for row in master_attendance:
            if row[1] == "NEED TO PAY":
                hunt_down.append(row[0])
    print("the following people need to pay dues: ")
    for person in hunt_down:
        print(person)
                

# function that makes a new master file for next cycle without header.
# copies over those who are carry-overs (PAID = N/A) and those
# who have paid or still need to pay.
def make_new_master(master):
    print("----------making new master file----------")
    new_master = []
    
    with open(master, 'rt') as f:
        read_master_attend = csv.reader(f)
        master_attendance = list(read_master_attend)

        carryover = 0
        for row in master_attendance:
            if row[1] == "N/A":
                for i in range(2, 11):
                    carryover = carryover + int(row[i])
                carryover_person = [row[0], 'N/A', '0', '0', '0', '0', '0', '0', \
                                    '0', '0', str(carryover)]
                print("carryover found: " + str(row[0]))
                new_master.append(carryover_person)
            carryover = 0
            if row[1] == "Paid":
                paid_person = [row[0], 'Paid', '0', '0', '0', '0', '0', '0', \
                                    '0', '0', '0']
                print("paid person found: " + str(row[0]))
                new_master.append(paid_person)
            if row[1] == "NEED TO PAY":
                need_pay_person = [row[0], 'NEED TO PAY', '0', '0', '0', '0', '0', '0', \
                                    '0', '0', '0']
                print("need pay person found: " + str(row[0]))
                new_master.append(need_pay_person)

        with open("new_master.csv", 'w', newline='') as outfile:
            writer = csv.writer(outfile)
            writer.writerows(new_master)

    print("new master file created.")
                
# function that makes copies of old master files and puts them in a folder
def make_copies(beg, inter, adv, master):

    directory = DATES[INDEX-1]
    
    if not os.path.exists(directory):
        print("creating directory: " + str(directory))
        os.makedirs(directory)
        
    if os.path.exists(directory):
        print("copying attendance files to directory: " + str(directory))
        shutil.copyfile(beg, os.path.join(directory, DATES[INDEX-1] + "_" + beg))
        shutil.copyfile(inter, os.path.join(directory, DATES[INDEX-1] + "_" + inter))
        shutil.copyfile(adv, os.path.join(directory, DATES[INDEX-1] + "_" + adv))
        shutil.copyfile(master, os.path.join(directory, DATES[INDEX-1] + "_" + master))

        print("copying input files to directory: " + str(directory))
        shutil.copyfile(BEGINNER_IN, os.path.join(directory, BEGINNER_IN))
        shutil.copyfile(INTERMEDIATE_IN, os.path.join(directory, INTERMEDIATE_IN))
        shutil.copyfile(ADVANCED_IN, os.path.join(directory, ADVANCED_IN))       


# function that sorts csv files alphabetically


# function that sorts csv files by paid, not paid



#####################################################################

# make_master_attendance(MASTER)
# make_song_attendance(BEG_ATTEND, INT_ATTEND, ADV_ATTEND)

# take_attendance(BEGINNER_IN, BEG_ATTEND)
# take_attendance(INTERMEDIATE_IN, INT_ATTEND)
# take_attendance(ADVANCED_IN, ADV_ATTEND)
# update_master(BEGINNER_IN, INTERMEDIATE_IN, ADVANCED_IN, MASTER)
# make_copies(BEG_ATTEND, INT_ATTEND, ADV_ATTEND, MASTER)


# need_pay(MASTER)
# update_paid(INTERMEDIATE_IN, MASTER)
# make_new_master(MASTER)


















