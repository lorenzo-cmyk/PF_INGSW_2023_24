# This script reads an xlsx file and converts each specified sheets into a JSON file.
# The JSON file will be named after the sheet name and will be saved in the same directory as the xlsx file.
# The JSON file will contain the data from the sheet in a list of dictionaries format.

# The script uses the pandas library to read the xlsx file and convert the data to JSON.
import pandas as pd

def convert_xlsx_to_json(xlsx_file, json_file, sheet_names):
    # Read the xlsx file into a pandas ExcelFile object
    xls = pd.ExcelFile(xlsx_file)

    # Initialize an empty dictionary to hold the dataframes
    dfs = {}

    # Loop through the sheet names and read each sheet into a dataframe
    for sheet_name in sheet_names:
        dfs[sheet_name] = xls.parse(sheet_name)

    # Convert each dataframe to JSON and save it to a file
    for sheet_name, df in dfs.items():
        df.to_json(f"{sheet_name}_{json_file}", orient='records')

# Parameters
xlsx_file = 'CardsCodexNaturalis_V1.0.xlsx'
json_file = 'CodexNaturalisCards.json'
sheet_names = ["ObjectiveCards", "ResourceCards", "GoldCards", "StartingCards"]

# Call the function to convert the xlsx file to JSON
convert_xlsx_to_json(xlsx_file, json_file, sheet_names)
