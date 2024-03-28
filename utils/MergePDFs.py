# Script to merge two PDF files into a single PDF file.
# The script assumes that the two PDF files have the same number of pages.
# If one PDF has more pages than the other, the extra pages are ignored.

# This script is used to merge the front and back of the CODEX cards into a single PDF file.

# Import the PyPDF2 library
import PyPDF2

# Define the paths to the two PDF files
front_file = 'CODEX_cards_gold_front.pdf'
back_file = 'CODEX_cards_gold_back.pdf'

# Open the PDF files
with open(front_file, 'rb') as front_pdf, open(back_file, 'rb') as back_pdf:
    # Create readers for the two PDFs
    front_reader = PyPDF2.PdfReader(front_pdf)
    back_reader = PyPDF2.PdfReader(back_pdf)
    
    # Create a writer for the new PDF
    writer = PyPDF2.PdfWriter()
    
    # Iterate through the pages of the two PDFs
    for i in range(max(len(front_reader.pages), len(back_reader.pages))):
        # Add the front page (if present)
        if i < len(front_reader.pages):
            writer.add_page(front_reader.pages[i])
        
        # Add the back page (if present)
        if i < len(back_reader.pages):
            writer.add_page(back_reader.pages[i])
    
    # Save the combined PDF
    with open('combined.pdf', 'wb') as combined_pdf:
        writer.write(combined_pdf)

# Print a message to indicate that the merge is complete
print("Merge complete. The 'combined.pdf' file has been created.")
