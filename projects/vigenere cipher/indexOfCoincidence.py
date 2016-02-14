# Owen Tian
# uses python v. 3.4.2



import re

# determines the index of coincidence of a string
def indexOfCoincidence(plaintext):

    counts = []
    totalcount = 0

    for i in range(26):
        counts.append(0)
        
    for i in range(len(plaintext)):
        index = ord(plaintext[i]) - 97
        # print("index is " + str(index))
        counts[index] = counts[index] + 1
        totalcount += 1

    # print(counts)
    # print(totalcount)

    sum = 0

    for i in range(26):
        sum = sum + counts[i] * (counts[i] - 1)

    ic = sum / (totalcount * (totalcount -1))
    # print("ic is " + str(ic))
    return ic

# takes text and breaks it into sequences based on period.
# example: if period is 3, then the text is broken into 3
# groups, each first letter, each second letter, and each third.
# then, it finds the average IC of each group
def find_avg_IC(text, period):
    if period < 1:
        print("ERROR: period is less than one")
    else:
        no_spaces = text.replace(" ", "")
        wordList = list(no_spaces)
        
        # print("wordList is " + str(wordList))

        # use nested for loop to put every nth letter into a list

        total_IC = 0

        for i in range(1, period + 1):

            # print("i is " + str(i))

            subtext = []
            pointer = 0
            initial_check = False

            while pointer < (len(wordList)):

                # print('subtext is ' + str(subtext))

                if (len(wordList) - pointer) <= period:
                    break
                
                if pointer == 0 and i == 1:
                    subtext.append(wordList[pointer])
                    pointer = pointer + period
                    initial_check = True
                    
                elif pointer == 0 and i > 1:
                    pointer = pointer + i - 1
                    subtext.append(wordList[pointer])

                else:
                    if (not initial_check):
                        pointer = pointer + period
                        subtext.append(wordList[pointer])

                    else:
                        subtext.append(wordList[pointer])
                        initial_check = False


            # print("subtext is " + str(subtext))
            ic = indexOfCoincidence(subtext)
            # print("ic of i = " + str(i) + " is " + str(ic))
            total_IC = total_IC + ic

        avg_IC = total_IC/period

        # print("total ic is " + str(total_IC))
        print("avg_IC of period = " + str(period) + " is " + str(avg_IC))

        return avg_IC


ciphertext = 'geagrdvzmc pctbq ekcrvpar wogjtfspkcs lhtflv vfagrd lmukcbl otv mcx ch kft ysy\
kfxguu kfpm mql apg fgcw dg ipwmgmiprrtem gebehwpk qtvitzrn bg uf rtkfkwgrtznp\
utty vyyi ggc tyc yfghstghnp dxgr yrwh tfqlls bh guupkr uemlwsp'


# find average IC's of all key lengths up to 30
for i in range(1, 31):
    find_avg_IC(ciphertext, i)




















    
        
        
