def synthesize_text(text):
    """Synthesizes speech from the input string of text."""
    from google.cloud import texttospeech
    client = texttospeech.TextToSpeechClient()

    input_text = texttospeech.types.SynthesisInput(text=text)

    # Note: the voice can also be specified by name.
    # Names of voices can be retrieved with client.list_voices().
    voice = texttospeech.types.VoiceSelectionParams(
        language_code='en-US',
        ssml_gender=texttospeech.enums.SsmlVoiceGender.FEMALE)

    audio_config = texttospeech.types.AudioConfig(
        audio_encoding=texttospeech.enums.AudioEncoding.MP3)

    response = client.synthesize_speech(input_text, voice, audio_config)

    # The response's audio_content is binary.
    with open('The Starry Night.mp3', 'wb') as out:
        out.write(response.audio_content)
        print('Audio content written to file "The Birth of Venus.mp3"')


text = "Painted between 1503 and 1517, Da Vinci’s alluring portrait has been dogged by two questions since the day it was made: Who’s the subject and why is she smiling? A number of theories for the former have been proffered over the years: That she’s the wife of the Florentine merchant Francesco di Bartolomeo del Giocondo (ergo, the work’s alternative title, La Gioconda); that she's Leonardo’s mother, Caterina, conjured from Leonardo's boyhood memories of her; and finally, that it's a self-portrait in drag. As for that famous smile, its enigmatic quality has driven people crazy for centuries. Whatever the reason, Mona Lisa’s look of preternatural calm comports with the idealized landscape behind her, which dissolves into the distance through Leonardo’s use of atmospheric perspective."
text2="Botticelli’s The Birth of Venus was the first full-length, non-religious nude since antiquity, and was made for Lorenzo de Medici. It’s claimed that the figure of the Goddess of Love is modeled after one Simonetta Cattaneo Vespucci, whose favors were allegedly shared by Lorenzo and his younger brother, Giuliano. Venus is seen being blown ashore on a giant clamshell by the wind gods Zephyrus and Aura as the personification of spring awaits on land with a cloak. Unsurprisingly, Venus attracted the ire of Savonarola, the Dominican monk who led a fundamentalist crackdown on the secular tastes of the Florentines. His campaign included the infamous “Bonfire of the Vanities” of 1497, in which “profane” objects—cosmetics, artworks, books—were burned on a pyre. The Birth of Venus was itself scheduled for incineration, but somehow escaped destruction. Botticelli, though, was so freaked out by the incident that he gave up painting for a while."
text3="Vincent Van Gogh’s most popular painting, The Starry Night was created by Van Gogh at the asylum in Saint-Rémy, where he’d committed himself in 1889. Indeed, The Starry Night seems to reflect his turbulent state of mind at the time, as the night sky comes alive with swirls and orbs of frenetically applied brush marks springing from the yin and yang of his personal demons and awe of nature."
synthesize_text(text3)

