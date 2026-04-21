import numpy as np

def process_audio(audio_bytes):
    """هنا يتم معالجة الصوت من Kotlin"""
    try:
        # تحويل البيانات إلى array
        audio_array = np.frombuffer(audio_bytes, dtype=np.int16)
        
        print(f"Received audio frame: {len(audio_array)} samples")
        
        # ←←← هنا ضع كود الـ Pipeline / AI الخاص بك لاحقاً
        
        return {
            "status": "success",
            "length": len(audio_array),
            "message": "Audio processed successfully"
        }
    except Exception as e:
        return {"status": "error", "message": str(e)}
