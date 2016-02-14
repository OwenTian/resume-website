# Owen Tian
# uses python v. 3.4.2




import math


# returns the chi-squared statistic of some text
# against english letter frequencies
def chi_squared(text):

    # remove spaces and lowercase everything
    no_spaces = text.replace(" ","")
    no_spaces = no_spaces.lower()

    # print(text)

    counts = []
    totalcount = 0
    

    # english letter frequencies
    expected = [0.08167,0.01492,0.02782,0.04253,0.12702,0.02228,0.02015,0.06094,\
                0.06966,0.00153,0.00772, 0.04025,0.02406,0.06749,0.07507,0.01929,\
                0.00095,0.05987,0.06327,0.09056,0.02758,0.00978, 0.02360,0.00150,\
                0.01974,0.00074]

    for i in range(26):
        counts.append(0)

    # print(len(no_spaces))

    for i in range(len(no_spaces)):
        index = ord(no_spaces[i]) - 97
        # print("index is " + str(index))
        counts[index] = counts[index] + 1
        totalcount += 1

    # print(counts)

    sum1 = 0

    for i in range(26):
        sum1 = sum1 + math.pow((counts[i] - totalcount*expected[i]), 2)/(totalcount*expected[i])


    # print("sum1 is " + str(sum1))
    return sum1

# returns all 26 possible caesar shifts of a string as a list of lists
def make_caesar(text):

    # convert all to lowercase and remove all spaces
    text = text.replace(" ", "")
    text = text.lower()

    all_shifts = []

    shift = 0

    while shift < 26:
        result_shift = []
        for i in range(len(text)):
            x = ord(text[i]) - 97
            x = (x - shift) % 26
            x = chr(x + 97)
            result_shift.append(x)

        # print("shift is " + str(shift))
        # print("appending " + str(result_shift))

        all_shifts.append(result_shift)
        shift = shift + 1

    '''
    test_chi = []
    for shift in all_shifts:
        test_chi.append(chi_squared(''.join(shift)))

    print(test_chi)
    '''

    return all_shifts

# takes a number index and converts it to its corresponding letter
def makeLetter(number):
    return chr(number + 97)

# split text into sequences based on period, then produces each
# possible caesar cipher of each sequence and runs chi_squared
# on each possible caesar cipher to obtain the lowest value
def lowest_chi(text, period):

    results = []

    if period < 1:
        print("ERROR: period is less than one")
    else:
        no_spaces = text.replace(" ", "")
        wordList = list(no_spaces)

    # split the text into sequences based on period
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

        # produce each possible caesar cipher of each sequence
        all_shifts = make_caesar(''.join(subtext))

        # run chi squared on each possible caesar cipher and return
        # the lowest value along with the letter it corresponds to
        all_chi = []
        
        for shift in all_shifts:
            all_chi.append(chi_squared(''.join(shift)))

        lowest_value = min(all_chi)
        low_index = all_chi.index(lowest_value)

        second_lowest = float("inf")

        for chi in all_chi:
            if chi < second_lowest and chi != lowest_value:
                second_lowest = chi

        second_index = all_chi.index(second_lowest)

        letter1 = makeLetter(low_index)
        letter2 = makeLetter(second_index)

        results.append([letter1, letter2])
        

        # print(all_chi)

        # results.append((lowest_value, all_chi.index(lowest_value)))

        

    return results


ciphertext = 'geagrdvzmc pctbq ekcrvpar wogjtfspkcs lhtflv vfagrd lmukcbl otv mcx ch kft ysy\
kfxguu kfpm mql apg fgcw dg ipwmgmiprrtem gebehwpk qtvitzrn bg uf rtkfkwgrtznp\
utty vyyi ggc tyc yfghstghnp dxgr yrwh tfqlls bh guupkr uemlwsp'


print("most probable letters: " + str(lowest_chi(ciphertext, 6)))


    

    

    
        
        


